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
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.useEffect
import react.useState
import web.cssom.*
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

val GachaRow = FC<GachaProps> { props ->
    val gacha = props.value
    table {
        width(100.pct)
        td {
            + gacha.nickName
            width(30.pct)
        }
        td {
            + "${gacha.ts}"
            width(25.pct)
        }
        td {
            + gacha.pool
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
    for (gacha in props.gachaList) {
        GachaRow {
            this.value = gacha
        }
    }
}


val App = FC<Props> {
    var displayGachaList by useState(listOf<Gacha>())
    useEffect(false) {
        val resp = ktorClient.get("/api/ark/gachas?page=0")
        val content = resp.bodyAsText()
        console.log(content)
        val data = Json.decodeFromString<GachaResponse>(content).data.list
        console.log(data)
        displayGachaList = data
    }

    GachaTable {
        gachaList = displayGachaList
    }
}