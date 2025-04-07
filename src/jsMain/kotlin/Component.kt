package top.moles

import emotion.react.css
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.quote
import js.globals.globalThis
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
import top.moles.ui.Card
import top.moles.ui.PageJumper
import web.cssom.*
import web.cssom.Globals.Companion.initial
import web.cssom.atrule.width
import top.moles.ui.ark.*

val scope = CoroutineScope(Dispatchers.Default)
val ktorClient = HttpClient(Js)





data class PageState(
    var uid: String?,
    var page: UInt,
)
val App = FC<Props> {
    var displayData by useState<GachaPage?>(null)
    var pageState by useState(PageState(null, 0u))
    fun jumpTo(newPage: UInt) {
        if (newPage == pageState.page) return
        pageState = pageState.copy(page = newPage)
    }

    useEffect(pageState) {
        val resp = ktorClient.get("/api/ark/gachas") {
            parameter("page", pageState.page)
            pageState.uid?.let {
                parameter("uid", it)
            }
        }
        val content = resp.bodyAsText()
        val data = Json.decodeFromString<GachaResponse>(content)
        displayData = data.data
    }
    Card {
        children = div.create {
            GachaTable {
                value = displayData?.list ?: emptyList()
            }
            displayData?.let {
                PageJumper {
                    value = it.pagination
                    onChange = {
                        pageState.page = it.newValue
                    }
                }
            }
        }
    }
    globalThis.set("jumpTo", fun(page: UInt, newUid: String?) {
        pageState.uid = newUid
        jumpTo(page)
    })

}