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
			}
		},
		/**
		 * 组件内部变量声明
		 */
		data() {
			return {
				// webView的UI实例,必须得用这个调用父组件的事件 $emit('')
				webViewUIHandler: null as WebViewUIHandler | null,
				// webView 实例对象,这个也可以直接使用 this.$el! 全局的函数
				webView: null as WebView | null,
			}
		},
		/**
		 * 属性变化监听器实现
		 */
		watch: {
			"webUrl": {
				/**
				 * 这里监听属性变化，并进行组件内部更新
				 */
				handler(newValue : string, oldValue : string) {
					console.log("newValue " + newValue)
					// console.log("oldValue "+oldValue)
					if (newValue != oldValue) {
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
		expose: ['doSomething', 'hideWebView', 'showWebView'],
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
			doSomething(param : string) {

			},
			
			hideWebView() {
				// console.log("this.$el=", this.$el)
				// console.log("this.$el=", this.webView)
				this.$el!.visibility = View.GONE
				// this.$el!.setVisibility(View.GONE);
				// this.$el!.setVisibility(View.GONE);
				// this.webView!.setVisibility(View.INVISIBLE);
				// 隐藏且不占用空间
				// this.$el!.setVisibility(View.GONE);
			},
			// 显示 WebView
			// 这里 有问题 但是 "hidden" watch 就是好的,不知道为什么 showWebView 就是不行 一样的代码
			showWebView() {
				
				this.$el!.visibility = View.VISIBLE
				// this.$el!.loadUrl("https://www.baidu.com");
				// // 确保父布局可见
				// let parent : ViewGroup = this.$el!.getParent() as ViewGroup;
				// if (parent.getVisibility() != View.VISIBLE) {
				// 	parent.setVisibility(View.VISIBLE);
				// }
				// this.$el!.setVisibility(View.VISIBLE);
				// // console.log("this.$el=",this.$el)
				// // console.log("this.$el=",this.webView)
				// // this.webView!.setVisibility(View.VISIBLE);
				// // this.$el!.setVisibility(View.VISIBLE);
				// // this.webView!.setVisibility(View.VISIBLE);
				// this.$el!.loadUrl("https://www.baidu.com"); // 重新加载内容
			},
			/**
			 * 内部使用的组件方法
			 */
			privateMethod() {

			}
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
			
			const webSettings = webView.getSettings();
			// 启用 JavaScript
			webSettings.setJavaScriptEnabled(true);
			// 启用 DOM 存储
			webSettings.setDomStorageEnabled(true);
			// 缩放至屏幕宽度
			webSettings.setLoadWithOverviewMode(true); 
			
			// 禁用硬件加速
			// webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); 
			
			// 请求定位权限
			webSettings.setGeolocationEnabled(true);
			
			// // 启用混合内容加载（HTTP 和 HTTPS）
			// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//     webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 允许加载混合内容
			// }
			// 监听事件 
			// 必须的 需要借助 UTSComponent 拿到webview的ui实例
			const webViewUIHandler = new WebViewUIHandler(this);
			webView.setWebViewClient(new MyWebViewClient(webViewUIHandler, webView));

			webView.loadUrl(this.webUrl)
			
			this.webViewUIHandler = webViewUIHandler;
			this.webView = webView;

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
			console.log("NVLayouted", this.webUrl);
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
		

		constructor(webViewUIHandler : WebViewUIHandler, webView : WebView) {
			super();
			this.webViewUIHandler = webViewUIHandler;
			this.webView = webView;
		}

		// 页面加载完成时的逻辑 可能会触发多次 不知道为什么
		override onPageFinished(view : WebView, url : string) : void {
			super.onPageFinished(view, url);
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
				console.log("url===",url)
				 // 拦截 HTTP 和 HTTPS 请求，让 WebView 正常加载
				        if (request.url.scheme == "http" || request.url.scheme == "https") {
				            return false // 返回 false 表示让 WebView 正常加载
				        }
		        return this.handleUrlLoading(url);
		    }
		
	// 	  override shouldOverrideUrlLoading(view: WebView, url: string): boolean {
	// 	          return this.handleUrlLoading(url);
	// 	      }
	
		 handleUrlLoading(url: string): boolean {
			  // 拦截 HTTP 和 HTTPS 请求，让 WebView 正常加载
       
			 //  url = request.getUrl().toString();
			 //         if (url.startsWith("http:") || url.startsWith("https:")) {
			 //             // 对于http和https协议的URL，直接在WebView中加载
			 //             view.loadUrl(url);
			 //             return true;
			 //         } else if (url.startsWith("intent://")) {
			 //             // 处理Intent Scheme
			 //             try {
			 //                 Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
			 //                 intent.addCategory("android.intent.category.BROWSABLE");
			 //                 intent.setComponent(null);
			 //                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			 //                     intent.setSelector(null);
			 //                 }
			 //                 List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
			 //                 if (resolves.size() > 0) {
			 //                     startActivityIfNeeded(intent, -1);
			 //                 }
			 //                 return true;
			 //             } catch (URISyntaxException e) {
			 //                 e.printStackTrace();
			 //             }
			 //         } else {
			 //             // 处理自定义Scheme
			 //             try {
			 //                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			 //                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 //                 startActivity(intent);
			 //                 return true;
			 //             } catch (Exception e) {
			 //                 // 防止没有安装的情况
			 //                 e.printStackTrace();
			 //                 Toast.makeText(context, "您所打开的第三方App未安装！", Toast.LENGTH_SHORT).show();
			 //             }
			 //         }
			 //         return false;

			 
			 
			 
			 
			 
			 
		        // 检查 URL 的协议
		        const uri = new URL(url);
		        const scheme = uri.protocol.replace(':', ''); // 去掉冒号
		
		        // 只允许 http 和 https 协议
		        if (scheme === 'http' || scheme === 'https') {
		              // 对于其他 URL，让 WebView 正常加载
		              return false;
		        } else {
		            // 拦截自定义协议
		            console.log("Blocked URL:", url);
					
					    let intent:Intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					    startActivity(intent);
					
				
					
		            return true; // 拦截请求，禁止加载
		        }
				
				
				
				
				
				
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