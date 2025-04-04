package top.moles

import emotion.react.css
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import react.FC
import react.Props
import react.PropsWithChildren
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import react.useEffect
import react.useState
import top.moles.commonComponent.ArkStyleUi
import web.cssom.*
import web.cssom.Globals.Companion.initial
import web.cssom.atrule.width

val scope = CoroutineScope(Dispatchers.Default)
val ktorClient = HttpClient(Js)

external interface CharRowProps : Props {
    var name: String
    var rarity: Rarity
}

val charRow = FC<CharRowProps> { props ->
    li {
        +props.name
        css {
            listStyleType = None.none
            color = when (props.rarity) {
                3 -> Color("#a231ff")
                4 -> Color("#cc7a00")
                5 -> Color("#ee5700")
                else -> NamedColor.black
            }
        }
    }
}

external interface GachaProps : Props {
    var value: Gacha
}

val gachaTd = FC<PropsWithChildren> {
    td {
        + children
    }
}

val GachaRow = FC<GachaProps> { props ->
    val gacha = props.value

    tr {
        td {
            +gacha.nickName
            width(30.pct)
        }
        td {
            +"${gacha.ts}"
            width(25.pct)
        }
        td {
            +gacha.pool
            width(25.pct)
        }
        td {
            width(25.pct)
            ol {
                gacha.chars.map { char ->
                    charRow {
                        name = char.name
                        rarity = char.rarity
                    }
                }
            }
        }
    }
}

external interface GachaTableProps : Props {
    var gachaList: List<Gacha>
}

val GachaTable = FC<GachaTableProps> { props ->
    table {
        css {
            width = 100.pct
            borderCollapse = BorderCollapse.separate
            textIndent = initial
            borderSpacing = 2.px
        }

        thead {
            tr {
                td { +"昵称" }
                td { +"时间" }
                td { +"寻访卡池" }
                td { +"获得干员" }
            }
            css {
                fontSize = 0.8.em
                color = Color("#158fc5")
                fontWeight = FontWeight.bolder
            }
        }

        tbody {
            css {
                fontSize = 0.75.em
            }
            for (gacha in props.gachaList) {
                GachaRow {
                    this.value = gacha
                }
            }
        }
    }
}


val App = FC<Props> {
    var displayData by useState<GachaPage?>(null)
    useEffect(false) {
        val resp = ktorClient.get("/api/ark/gachas?page=0")
        val content = resp.bodyAsText()
        console.log(content)
        val data = Json.decodeFromString<GachaResponse>(content)
        console.log(data)
        displayData = data.data
    }
    ArkStyleUi.Card {
        children = GachaTable.create {
            gachaList = displayData?.list ?: emptyList()
        }
    }

}