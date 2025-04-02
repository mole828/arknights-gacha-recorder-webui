package top.moles.components

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useState

val App = FC<Props> {
    var text by useState("hello")
    div {
        for (i in 1..10) {
            h1 {
                +text
            }
        }
        button {
            +"click me"
            onClick = {
                text += "!"
            }
        }
    }
}