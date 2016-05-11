package com.charles.weibo.entity;
import org.json.JSONObject;

/**
 * 
 * @author Charles
 *	微博评论Model
 */

public class CommentM {

	private String created_at ;   				//评论创建时间
	private long id ; 				    		//评论的ID
	private String Text  ; 						//评论的内容
	private String source ; 					//评论的来源
	private UserModel user  ;					//评论作者的用户信息字段
	private String mid ;						//评论的MID
	private String idstr ; 						//字符串型的评论ID
	private WeiboModel status ;					//评论的微博信息字段
	private ReplyComment reply_comment ; 		//评论来源评论，当本评论属于对另一评论的回复时返回此字段
	private int total_number ;
	
	public CommentM (){}
	
	public CommentM(String created_at, Long id, String text, String source,
			UserModel user, String mid, String idstr, WeiboModel status,
			ReplyComment reply_comment, int total_number) {
		super();
		this.created_at = created_at;
		this.id = id;
		Text = text;
		this.source = source;
		this.user = user;
		this.mid = mid;
		this.idstr = idstr;
		this.status = status;
		this.reply_comment = reply_comment;
		this.total_number = total_number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStatus(WeiboModel status) {
		this.status = status;
	}

	public void setReply_comment(ReplyComment reply_comment) {
		this.reply_comment = reply_comment;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		String []s =source.split("rel=\"nofollow\">");
		source ="來自"+s[1].replace("</a>", "");
		this.source = source;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	public WeiboModel getStatus() {
		return status;
	}
	public void setStatus(JSONObject status) {
		WeiboModel weico = new WeiboModel();
		try{
		weico.setCreated_at(status.getString("created_at"));
		weico.setId(status.getLong("id"));
		weico.setIdstr(status.getString("idstr"));
		weico.setText(status.getString("text"));
		UserModel userOld = new UserModel (status.optJSONObject("user")); 
		weico.setReposts_count(status.getInt("reposts_count"));
		weico.setComments_count(status.getInt("comments_count"));
		weico.setAttitudes_count(status.getInt("attitudes_count"));
		weico.setUser(userOld);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.status = weico;
	}
	public ReplyComment getReply_comment() {
		return reply_comment;
	}
	public void setReply_comment(JSONObject reply_comment) {
		ReplyComment rc = new ReplyComment() ;
		try {
			rc.setCreated_at(reply_comment.getString("created_at"));
			rc.setId(reply_comment.getLong("id"));
			rc.setSource(reply_comment.getString("source"));
			rc.setSource_allowclick(reply_comment.getInt("source_allowclick"));
			rc.setSource_type(reply_comment.getInt("source_type"));
			rc.setText(reply_comment.getString("text"));
			UserModel user = new UserModel(reply_comment.optJSONObject("user"));
			rc.setUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		this.reply_comment = rc;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}
	
	public class ReplyComment{
		private String created_at;
		private Long id ;
		private String Text ;
		private int source_allowclick;
		private int source_type ;
		private String source ;
		private UserModel user ;  
		
		public UserModel getUser() {
			return user;
		}
		public void setUser(UserModel user) {
			this.user = user;
		}
		public String getCreated_at() {
			return created_at;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getText() {
			return Text;
		}
		public void setText(String text) {
			Text = text;
		}
		public int getSource_allowclick() {
			return source_allowclick;
		}
		public void setSource_allowclick(int source_allowclick) {
			this.source_allowclick = source_allowclick;
		}
		public int getSource_type() {
			return source_type;
		}
		public void setSource_type(int source_type) {
			this.source_type = source_type;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		
	}
}
