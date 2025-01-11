<template>
	<view>
	</view>
</template>

<script lang="uts">
	/**
   * 引用 Android 系统库
   * [可选实现，按需引入]
   */
	import TextUtils from 'android.text.TextUtils';
	import View from 'android.view.View';
	import WebView from 'android.webkit.WebView';
	import WebSettings from 'android.webkit.WebSettings';
	import WebViewClient from 'android.webkit.WebViewClient';
	import ValueCallback from 'android.webkit.ValueCallback';
	import ViewGroup from 'android.view.ViewGroup';
	import Build from 'android.os.Build';
	import WebResourceRequest from 'android.webkit.WebResourceRequest';
	import URL from 'java.net.URL';
	import Intent from 'android.content.Intent';
	import Uri from 'android.net.Uri';
	import WebBackForwardList from 'android.webkit.WebBackForwardList';
	import WebHistoryItem from 'android.webkit.WebHistoryItem';
	import WeakReference from 'java.lang.ref.WeakReference';
	import Handler from 'android.os.Handler';
	import Looper from 'android.os.Looper';
	import ProgressBar from 'android.widget.ProgressBar';

	/**
	 * 引入三方库
	 * [可选实现，按需引入]
	 *
	 * 在 Android 平台引入三方库有以下两种方式：
	 * 1、[推荐] 通过 仓储 方式引入，将 三方库的依赖信息 配置到 config.json 文件下的 dependencies 字段下。详细配置方式[详见](https://uniapp.dcloud.net.cn/plugin/uts-plugin.html#dependencies)
	 * 2、直接引入，将 三方库的aar或jar文件 放到libs目录下。更多信息[详见](https://uniapp.dcloud.net.cn/plugin/uts-plugin.html#android%E5%B9%B3%E5%8F%B0%E5%8E%9F%E7%94%9F%E9%85%8D%E7%BD%AE)
	 *
	 * 在通过上述任意方式依赖三方库后，使用时需要在文件中 import
	 * import { LottieAnimationView } from 'com.airbnb.lottie.LottieAnimationView'
	 */

	/**
	 * UTSAndroid 为平台内置对象，不需要 import 可直接调用其API，[详见](https://uniapp.dcloud.net.cn/uts/utsandroid.html#utsandroid)
	 */

	//原生提供以下属性或方法的实现
	export default {
		/**
		 * 组件名称，也就是开发者使用的标签
		 */
		name: "cqy-web-view",
		/**
		 * 组件涉及的事件声明，只有声明过的事件，才能被正常发送
		 */
		emits: ['updateHtml', 'setWebUrl'],
		/**
		 * 属性声明，组件的使用者会传递这些属性值到组件
		 */
		props: {
			"webUrl": {
				type: String,
				default: ""
			},
			/**
					   * 是否隐藏动画
					   */
			"hidden": {
				type: Boolean,
				default: true
			},
			/**
			 * 前进或者后退的方法
			 */
			"back":{
				type: Boolean,
				default: true
			},
			"destructionWebView":{
				type: Boolean,
				default: true
			}
		},
		/**
		 * 组件内部变量声明
		 */
		data() {
			return {
				// webView的UI实例,必须得用这个调用父组件的事件 $emit('')
				webViewUIHandler: null as WebViewUIHandler | null,
				// webView 实例对象,这个也可以直接使用 this.$el! 全局的函数 有问题 不生效
				webViewData: null as WebView | null,
			}
		},
		/**
		 * 属性变化监听器实现
		 */
		watch: {
			"webUrl": {
				handler(newValue : string, oldValue : string) {
					console.log("newValue " + newValue)
					if (newValue != oldValue) {
						// UTSAndroid.getDispatcher("main").async(function(_){
						// 	let url:string | null =  this.webViewData?.getUrl();
						// 	if (url !=null  && newValue != url) {
								console.log("赋值 newValue " + newValue)
						// 		this.webViewData?.loadUrl(newValue);
						// 	}
						// },null)
						
						this.$el!.loadUrl(newValue);
					}
				},
				immediate: false // 创建时是否通过此方法更新属性，默认值为false
			},
			"hidden": {
				handler(newValue : boolean) {
					if (this.$el != null) {
						if (newValue) {
							this.$el!.visibility = View.VISIBLE
						} else {
							this.$el!.visibility = View.GONE
						}
					}
				},
				immediate: false
			}
		},
		/**
		 * 规则：如果没有配置expose，则methods中的方法均对外暴露，如果配置了expose，则以expose的配置为准向外暴露
		 * ['publicMethod'] 含义为：只有 `publicMethod` 在实例上可用
		 */
		expose: [ 'hideWebView', 'showWebView','back','forward','destructionWebView','reload'],
		// 这里面不支持回调函数
		methods: {
			/**
			 * 对外公开的组件方法
			 *
			 * uni-app中调用示例：
			 * this.$refs["组件ref"].doSomething("uts-button");
			 *
			 * uni-app x中调用示例：
			 * 1、引入对应Element
			 * import { UtsButtonElement(组件名称以upper camel case方式命名 + Element) } from 'uts.sdk.modules.utsComponent(组件目录名称以lower camel case方式命名)';
			 * 2、(this.$refs["组件ref"] as UtsButtonElement).doSomething("uts-button");
			 * 或 (uni.getElementById("组件id") as UtsButtonElement).doSomething("uts-button");
			 */
			back(){
				console.log("getBack")
				UTSAndroid.getDispatcher("main").async(function(_){
					this.webViewData?.goBack()
				},null)
				
			},
			// 前进到下一个页面
			forward(){
				console.log("goForward")
			UTSAndroid.getDispatcher("main").async(function(_){
				this.webViewData?.goForward()
			},null)
			},
			reload(){
				UTSAndroid.getDispatcher("main").async(function(_){
					this.webViewData?.reload()
				},null)
			},
			destructionWebView(){
				console.log("destructionWebView")
				UTSAndroid.getDispatcher("main").async(function(_){
					this.webViewData?.destroy();
				},null)
			},
			hideWebView() {
				this.$el!.visibility = View.GONE
			},
			showWebView() {
				this.$el!.visibility = View.VISIBLE
			},
		},
		/**
		 * [可选实现] 组件被创建，组件第一个生命周期，
		 * 在内存中被占用的时候被调用，开发者可以在这里执行一些需要提前执行的初始化逻辑
		 */
		created() {
			console.log("created")
			
		},
		/**
		 * [可选实现] 对应平台的view载体即将被创建，对应前端beforeMount
		 */
		NVBeforeLoad() {
			console.log("NVBeforeLoad")
		},
		/**
		 * [必须实现] 创建原生View，必须定义返回值类型
		 * 开发者需要重点实现这个函数，声明原生组件被创建出来的过程，以及最终生成的原生组件类型
		 * （Android需要明确知道View类型，需特殊校验）
		 * 
		 * 
		 * 这个事件太慢了 父组件完全加载完毕了 这个才初始化,提前赋值是不行的,ui实例没创建出来
		 */
		NVLoad() : WebView {
			let webView = new WebView($androidContext!);
			this.webViewData = webView;
			
			const webSettings = webView.getSettings();
			// 启用 JavaScript
			webSettings.setJavaScriptEnabled(true);
			// 启用 DOM 存储
			webSettings.setDomStorageEnabled(true);
			// 缩放至屏幕宽度
			webSettings.setLoadWithOverviewMode(true); 
			
			// 请求定位权限
			webSettings.setGeolocationEnabled(true);
			
			// 监听事件 
			// 必须的 需要借助 UTSComponent 拿到webview的ui实例
			const webViewUIHandler = new WebViewUIHandler(this);
			webView.setWebViewClient(new MyWebViewClient(webViewUIHandler, webView));

			// webView.loadUrl(this.webUrl)
			
			this.webViewUIHandler = webViewUIHandler;

			return webView;
		},
		/**
		 * [可选实现] 原生View已创建
		 */
		NVLoaded() {

		},
		/**
		 * [可选实现] 原生View布局完成
		 */
		NVLayouted() {
			console.log("NVLayouted 原生View布局完成");
			// 原生View布局完成 去调用父组件 告诉他 赋值 webUrl到这个组件上面来
			this.webViewUIHandler?.setWebUrl();
		},
		/**
		 * [可选实现] 原生View将释放
		 */
		NVBeforeUnload() {
			console.log("NVBeforeUnload")
		},
		/**
		 * [可选实现] 原生View已释放，这里可以做释放View之后的操作
		 */
		NVUnloaded() {
			console.log("NVUnloaded")
		},
		/**
		 * [可选实现] 组件销毁
		 */
		unmounted() {
			console.log("unmounted")
		},
		/**
		 * [可选实现] 自定组件布局尺寸，用于告诉排版系统，组件自身需要的宽高
		 * 一般情况下，组件的宽高应该是由终端系统的排版引擎决定，组件开发者不需要实现此函数
		 * 但是部分场景下，组件开发者需要自己维护宽高，则需要开发者重写此函数
		 * 
		 * HBuilder X 4.25 版本后新增 UTSMeasureMode 参数
		 */
		NVMeasure(size : UTSSize, mode : UTSMeasureMode) : UTSSize {
			return size;
		}
	}

	// 处理js执行完毕之后的回调 拿到页面的元素信息
	class ValueCallbackMethod extends ValueCallback<string>{
		private webViewUIHandler : WebViewUIHandler;

		constructor(webViewUIHandler : WebViewUIHandler) {
			super();
			this.webViewUIHandler = webViewUIHandler;

		}
		override onReceiveValue(value : string) : void {
			// 直接执行 $emit("event",Any)	
			// this.$emit!("updateHtml", {
			// 	cqyData: value
			// })
			this.webViewUIHandler.handleClick(value);
		}
	}


	// 自定义 WebViewClient
	class MyWebViewClient extends WebViewClient {
		
		private webViewUIHandler : WebViewUIHandler;

		private webView : WebView;
		
		// 是否执行过了 避免 onPageFinished 执行多次 
		private  isPageFinishedExecuted :boolean = false; // 标志位


		constructor(webViewUIHandler : WebViewUIHandler, webView : WebView) {
			super();
			this.webViewUIHandler = webViewUIHandler;
			this.webView = webView;
		}

		// 页面加载完成时的逻辑 可能会触发多次 不知道为什么
		override onPageFinished(view : WebView, url : string) : void {
			super.onPageFinished(view, url);
			
			
				this.isPageFinishedExecuted = true; // 设置标志位，确保只执行一次
				// html: encodeURIComponent(document.documentElement.outerHTML), // 对 HTML 进行 URL 编码
				view.evaluateJavascript(
					`
					(function() {
					    const data = {
					        title: document.title,
					        host: window.location.host
					    };
					    return data;
					})();
					`,
					new ValueCallbackMethod(this.webViewUIHandler)
				);
		}
		
		override shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): boolean {
		        const url = request.getUrl().toString();
				 // 拦截 HTTP 和 HTTPS 请求，让 WebView 正常加载
				        if (request.url.scheme == "http" || request.url.scheme == "https") {
							        // view.loadUrl(url) // 让 WebView 处理 HTTP/HTTPS 请求
				            return false // 返回 false 表示让 WebView 正常加载
				        }
		        return this.handleUrlLoading(url,view);
		    }
		
	
		 handleUrlLoading(url: string,view: WebView): boolean {
		            // 拦截自定义协议
		            console.log("Blocked URL:", url);
					    let intent:Intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					    // startActivity($androidContext,intent);
					view.context.startActivity(intent)
				
		            return true; // 拦截请求，禁止加载
				
		    }
	}


	// 获取ui的实例 才能使用 $emit
	class WebViewUIHandler {
		private comp : UTSComponent<WebView>;

		constructor(comp : UTSComponent<WebView>) {
			this.comp = comp;
		}
		// 传输js的代码给父组件
		handleClick(data : string) {
			this.comp.$emit("updateHtml", {
				cqyData: data
			});
		}
		// 告诉父组件 可以把url传给webVIew了
		setWebUrl() {
			this.comp.$emit("setWebUrl");
		}

	}
</script>

<style>

</style>