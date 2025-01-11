@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER")
package uts.sdk.modules.cqyWebView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.component.WXComponentProp;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.uniapp.*;
import io.dcloud.uniapp.extapi.*;
import io.dcloud.unicloud.*;
import io.dcloud.uts.*;
import io.dcloud.uts.Map;
import io.dcloud.uts.Set;
import io.dcloud.uts.UTSAndroid;
import io.dcloud.uts.component.*;
import io.dcloud.uts.component.UTSComponent;
import io.dcloud.uts.component.UTSMeasureMode;
import io.dcloud.uts.component.UTSSize;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.async;
open class CqyWebViewComponent : UTSComponent<WebView> {
    constructor(instance: UniSDKInstance?, parent: AbsVContainer<*>?, componentData: AbsComponentData<*>?) : super(instance, parent, componentData) ;
    open var webUrl: String = "";
    open var hidden: Boolean = true;
    private var webViewUIHandler: WebViewUIHandler? = null;
    private var webView: WebView? = null;
    override fun created() {
        console.log("created", " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:171");
    }
    override fun NVBeforeLoad() {
        console.log("NVBeforeLoad", " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:178");
    }
    override fun NVLoad(): WebView {
        var webView = WebView(`$androidContext`!!);
        val webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        val webViewUIHandler = WebViewUIHandler(this);
        webView.setWebViewClient(MyWebViewClient(webViewUIHandler, webView));
        webView.loadUrl(this.webUrl);
        this.webViewUIHandler = webViewUIHandler;
        this.webView = webView;
        return webView;
    }
    override fun NVLoaded() {}
    override fun NVLayouted() {
        console.log("NVLayouted", this.webUrl, " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:231");
        this.webViewUIHandler?.setWebUrl();
    }
    override fun NVBeforeUnload() {
        console.log("NVBeforeUnload", " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:239");
    }
    override fun NVUnloaded() {
        console.log("NVUnloaded", " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:245");
    }
    override fun unmounted() {
        console.log("unmounted", " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:251");
    }
    override fun NVMeasure(size: UTSSize, mode: UTSMeasureMode): UTSSize {
        return size;
    }
    @JSMethod(uiThread = false)
    open fun doSomething(param: String) {}
    @JSMethod(uiThread = false)
    open fun hideWebView() {
        this.`$el`!!.visibility = View.GONE;
    }
    @JSMethod(uiThread = false)
    open fun showWebView() {
        this.`$el`!!.visibility = View.VISIBLE;
    }
    open fun privateMethod() {}
    override fun `$init`() {
        this.`$watch`<String>("webUrl", fun(newValue, oldValue){
            console.log("newValue " + newValue, " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:86");
            if (newValue != oldValue) {
                this.`$el`!!.loadUrl(newValue);
            }
        }
        );
        this.`$watch`<Boolean>("hidden", fun(newValue){
            if (this.`$el` != null) {
                if (newValue) {
                    this.`$el`!!.visibility = View.VISIBLE;
                } else {
                    this.`$el`!!.visibility = View.GONE;
                }
            }
        }
        );
    }
    @WXComponentProp(name = "webUrl")
    open fun componentSetWebUrl(value: String) {
        this.webUrl = value;
        this.`$componentWatchDispatch`("webUrl", value);
    }
    @WXComponentProp(name = "hidden")
    open fun componentSetHidden(value: Boolean) {
        this.hidden = value;
        this.`$componentWatchDispatch`("hidden", value);
    }
}
open class ValueCallbackMethod : ValueCallback<String> {
    private var webViewUIHandler: WebViewUIHandler;
    constructor(webViewUIHandler: WebViewUIHandler) : super() {
        this.webViewUIHandler = webViewUIHandler;
    }
    override fun onReceiveValue(value: String): Unit {
        this.webViewUIHandler.handleClick(value);
    }
}
open class MyWebViewClient : WebViewClient {
    private var webViewUIHandler: WebViewUIHandler;
    private var webView: WebView;
    constructor(webViewUIHandler: WebViewUIHandler, webView: WebView) : super() {
        this.webViewUIHandler = webViewUIHandler;
        this.webView = webView;
    }
    override fun onPageFinished(view: WebView, url: String): Unit {
        super.onPageFinished(view, url);
        view.evaluateJavascript("\n				(function() {\n				    const data = {\n				        title: document.title,\n				        host: window.location.host\n				    };\n				    return data;\n				})();\n				", ValueCallbackMethod(this.webViewUIHandler));
    }
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.getUrl().toString();
        console.log("url===", url, " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:318");
        if (request.url.scheme == "http" || request.url.scheme == "https") {
            return false;
        }
        return this.handleUrlLoading(url, view);
    }
    open fun handleUrlLoading(url: String, view: WebView): Boolean {
        console.log("Blocked URL:", url, " at uni_modules/cqy-web-view/utssdk/app-android/index.vue:333");
        var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP);
        view.context.startActivity(intent);
        return true;
    }
}
open class WebViewUIHandler {
    private var comp: UTSComponent<WebView>;
    constructor(comp: UTSComponent<WebView>){
        this.comp = comp;
    }
    open fun handleClick(data: String) {
        this.comp.`$emit`("updateHtml", object : UTSJSONObject() {
            var cqyData = data
        });
    }
    open fun setWebUrl() {
        this.comp.`$emit`("setWebUrl");
    }
}
