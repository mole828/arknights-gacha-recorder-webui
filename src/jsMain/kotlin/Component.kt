package top.moles

import emotion.react.css
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import js.core.BigInt
import js.intl.DateTimeFormat
import js.temporal.Instant
import kotlinx.coroutines.*
import kotlinx.datetime.LocalDate
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
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

val gachaRow = FC<GachaProps> { props ->
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


val App = FC<Props> {
    val displayGachas = useState( mutableListOf<Gacha>())
    div {
        scope.launch {
            val resp = ktorClient.get("/api/ark/gachas?page=0")
            console.log(resp.bodyAsText())

        }
        gachaRow {
            this.value = Gacha(
                chars = 1.until(6).map {
                    Char(
                        isNew = true,
                        rarity = it,
                        name = "草饲"
                    )
                },
                uid = "123",
                nickName = "鼹鼠#123",
                ts = 1,
                pool = "a",
            )
        }
    }
}