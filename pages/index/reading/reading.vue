<template>
	<view>
		<button @click="goto('https://www.zhihu.com/question/655754543/answer/40152692683')"> 知乎跳转1</button>
		<button @click="goto('https://juejin.cn/post/7419515465560588298?utm_source=gold_browser_extension')"> 掘金跳转2</button>
		<button @click="goto('https://modstart.com/p/ghxv5dyrm4vexr35')"> 跳转3</button>
		<button @click="goto('https://github.com/43278047/utsDemo')"> 跳转4</button>
		<button @click="goto('https://jingyan.baidu.com/article/870c6fc326ef94f13ee4be26.html')"> 跳转5</button>
		<button @click="goto('https://www.ithome.com/0/823/951.htm?utm_source=gold_browser_extension')"> 跳转6</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			goto(url){
				// 对复杂参数进行编码
				const encodedUserInfo = encodeURIComponent(url);
				
				// 跳转并传递参数
				uni.navigateTo({
				  url: `/pages/index/index?url=${encodedUserInfo}`,
				});

			}
		}
	}
</script>

<style>

</style>
