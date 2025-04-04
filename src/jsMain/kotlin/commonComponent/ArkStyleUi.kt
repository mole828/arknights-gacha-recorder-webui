package top.moles.commonComponent

import emotion.react.css
import react.ChildrenBuilder
import react.FC
import react.Props
import react.PropsWithChildren
import react.PropsWithValue
import react.ReactElement
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import web.cssom.BoxShadow
import web.cssom.BoxSizing
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.em
import web.cssom.pct
import web.cssom.px
import kotlin.reflect.KCallable


external interface CardProps: PropsWithChildren

object ArkStyleUi {
    val Card = FC<CardProps> { props ->
        div {
            css {
                minWidth = 20.em
                minHeight = 8.em
                backgroundColor = Color("hsla(0,0%,100%,.8)")
                borderRadius = 1.2.em
                boxShadow = BoxShadow(0.px, 2.px, 10.px, (-5).px, Color("rgb(9 2 4)"))
                padding = 2.em
                boxSizing = BoxSizing.borderBox

                width = 98.pct
                margin = 1.pct
            }
            +props.children
        }
    }
}
