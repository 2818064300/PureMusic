package com.lie.puremusic.music.netease.data

data class CommentData(
    val code: Int,
    val hotComments: List<HotComment>, // 热门评论
    val total: Long // 总评论
) {
    data class HotComment(
        val user: CommentUser,
        val content: String, // 评论内容
        val time: Long, // 评论时间
        val likedCount: Long // 点赞数
    ){
        data class CommentUser(
            val avatarUrl: String, // 头像
            val nickname: String, // 昵称
            val userId: Long //
        )
    }
}