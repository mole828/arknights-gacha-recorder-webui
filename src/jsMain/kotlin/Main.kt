package top.moles


import react.Fragment
import react.create
import react.dom.client.createRoot
import react.useState
import top.moles.components.App
import web.dom.document
import web.html.HTML.div


fun main() {
    val container = document.createElement(div)
    document.body.appendChild(container)
    createRoot(container).render(App.create())
}