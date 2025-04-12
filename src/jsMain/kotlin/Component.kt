package top.moles

import emotion.react.css
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import js.globals.globalThis
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import react.*
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.div
import top.moles.ui.Antd
import top.moles.ui.Card
import top.moles.ui.PageJumper
import top.moles.ui.SelectOption
import top.moles.ui.ark.*
import web.cssom.*


val scope = CoroutineScope(Dispatchers.Default)
val ktorClient = HttpClient(Js)
val json = Json {
    ignoreUnknownKeys = true
}





data class PageState(
    var uid: String?,
    var page: UInt,
) {
    companion object {
        val firstPage = 0u
    }
}
val App = FC<Props> {
    var displayData by useState<GachaPage?>(null)
    var pageState by useState(PageState(null, 0u))
    var userList by useState<List<User>>(emptyList())
    fun jumpTo(newPage: UInt) {
        if (newPage == pageState.page) return
        pageState = pageState.copy(page = newPage)
    }

    useEffect(false) {
        val resp = ktorClient.get("/api/ark/users")
        val content = resp.bodyAsText()
        val data = json.decodeFromString<List<User>>(content)
        userList = data
    }
    useEffect(pageState) {
        val resp = ktorClient.get("/api/ark/gachas") {
            parameter("page", pageState.page)
            pageState.uid?.let {
                parameter("uid", it)
            }
        }
        val content = resp.bodyAsText()
        val data = json.decodeFromString<GachaResponse>(content)
        displayData = data.data
    }
    Card {
        children = div.create {
            a {
                + "搜索:"
                css {
                    color = Color("#158fc5")
                }
            }
            Antd.Select {
                css {
                    width = 60.pct
                    marginLeft = 1.em
                }
                showSearch = true
                optionFilterProp = "label"
                options = userList.map {
                    SelectOption(
                        value = it.uid,
                        label = it.nickName,
                        key = it.uid,
                    )
                }.toTypedArray()
                onChange = { value ->
                    pageState = pageState.copy(uid = value, page = PageState.firstPage)
                }
            }
            displayData?.let { displayData ->
                GachaTable {
                    value = displayData.list
                }
                PageJumper {
                    value = displayData.pagination
                    onChange = {
                        pageState = pageState.copy(page = it.newValue)
                    }
                }
            }
            div {
                css {
                    fontSize = (0.75).em
                    color = rgb(86,86,86)
                    margin = Margin(1.em, 1.em)
                }
                + "* 可查询"
                b {
                    + "不止 30日 100条 以内"
                }
                + "有效数据."
            }
            div {
                css {
                    fontSize = (0.75).em
                    color = rgb(86,86,86)
                    margin = Margin(1.em, 1.em)
                }
                + "** 查询结果可能与游戏内实际操作存在延迟."
            }
        }
    }
    globalThis.set("jumpTo", fun(page: UInt, newUid: String?) {
        pageState.uid = newUid
        jumpTo(page)
    })
    globalThis.set("antd", Antd)

}