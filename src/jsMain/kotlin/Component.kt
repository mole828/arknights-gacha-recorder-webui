package top.moles

import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.useState
import web.cssom.Color


external interface CharRowProps: Props {
    var name: String
    var rarity: Rarity
}
val charRow = FC<CharRowProps> { props ->
    li {
        + props.name
        css {
            color = when(props.rarity) {
                3 -> Color("#a231ff")
                else -> throw Error("unknow")
            }
        }
    }
}
external interface GachaProps: Props {
    var value: Gacha
}
val gachaRow = FC<GachaProps> { props ->
    val gacha = props.value
    div {
        charRow {
            name = "炜草"
            rarity = 1
        }
    }
}


val App = FC<Props> {
    var text by useState("hello")
    div {
        for (i in 1..7) {
            gachaRow{}
        }
        button {
            +"click me"
            onClick = {
                text += "!"
            }
        }
    }
}