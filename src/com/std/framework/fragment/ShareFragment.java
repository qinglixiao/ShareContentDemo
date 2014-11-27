package com.std.framework.fragment;

import java.io.File;
import java.util.Date;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.std.framework.R;
import com.std.framework.view.CustomShareBoard;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMEvernoteHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.media.EvernoteShareContent;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.facebook.controller.UMFacebookHandler.PostType;
import com.umeng.socialize.facebook.media.FaceBookShareContent;
import com.umeng.socialize.instagram.controller.UMInstagramHandler;
import com.umeng.socialize.instagram.media.InstagramShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.linkedin.controller.UMLinkedInHandler;
import com.umeng.socialize.linkedin.media.LinkedInShareContent;
import com.umeng.socialize.media.GooglePlusShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.pinterest.controller.UMPinterestHandler;
import com.umeng.socialize.pinterest.media.PinterestShareContent;
import com.umeng.socialize.pocket.controller.UMPocketHandler;
import com.umeng.socialize.pocket.media.PocketShareContent;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;

/**
 * @功能描述 : 分享页面Fragment,包括分享、图文分享、快速分享等
 * 
 * @原 作 者 :
 * @版 本 号 : [版本号, Aug 8, 2013]
 * 
 * @修 改 人 : mrsimple
 * @修改内容 :
 */
/**
 *
 */
public class ShareFragment extends Fragment implements OnClickListener {
	public static final String UMSERVICE = "com.umeng.share";

	private final UMSocialService mController = UMServiceFactory.getUMSocialService(UMSERVICE);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_share, container, false);
		initViewListener(rootView);
		// 配置需要分享的相关平台
		configPlatforms();
		// 设置分享的内容
		setShareContent();
		return rootView;
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// 添加人人网SSO授权
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(), "201874", "28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 初始化视图控件，比如Button
	 */
	private void initViewListener(View rootView) {
		// 默认分享页
		rootView.findViewById(R.id.btn_default).setOnClickListener(this);
		// 自定义分享页
		rootView.findViewById(R.id.btn_custom).setOnClickListener(this);
		//        // 底层分享
		//        rootView.findViewById(R.id.direct_share).setOnClickListener(this);
		//        // 一键分享到所有已经授权的平台
		//        rootView.findViewById(R.id.share_multi).setOnClickListener(this);
		//        // 添加支持的所有平台
		//        rootView.findViewById(R.id.share_all_platform).setOnClickListener(this);

	}

	/**
	 * 点击事件处理。</br> 在不考虑是否授权的情况下区别openShare、postShare、directShare三个方法 <li>
	 * openShare : 分享面板 -> 分享编辑页 -> 分享 <li>postShare : 分享编辑页 -> 分享 <li>
	 * directShare : 分享
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_default) {
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
					SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
			mController.openShare(getActivity(), false);
		}
		else if (id == R.id.btn_custom) {
			postShare();
		}
		//        } else if (id == R.id.direct_share) {
		//            directShare();
		//        } else if (id == R.id.share_multi) {
		//            shareMult();
		//        } else if (id == R.id.share_all_platform) {
		//            addCustomPlatforms();
		//        }

	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		CustomShareBoard shareBoard = new CustomShareBoard(getActivity());
		shareBoard.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	/**
	 * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
	 */
	private void directShare() {
		mController.directShare(getActivity(), mPlatform, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				String showText = "分享成功";
				if (eCode != StatusCode.ST_CODE_SUCCESSED) {
					showText = "分享失败 [" + eCode + "]";
				}
				Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 一键分享到多个已授权平台。</br>
	 */
	private void shareMult() {
		SHARE_MEDIA[] platforms = new SHARE_MEDIA[] { SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN };
		mController.postShareMulti(getActivity(), new MulStatusListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
				String showText = "分享结果：" + multiStatus.toString();
				Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
			}
		}, platforms);
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), "100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能");

		// APP ID：201874, API
		// * KEY：28401c0964f04a72a14c812d6132fcef, Secret
		// * Key：3bf66e42db1e4fa9829b955cc300b737.
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(), "201874", "28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		UMImage localImage = new UMImage(getActivity(), R.drawable.device);
		UMImage urlImage = new UMImage(getActivity(), "http://www.umeng.com/images/pic/social/integrated_3.png");
		// UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

		// 视频分享
		UMVideo video = new UMVideo("http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		// vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		video.setTitle("友盟社会化组件视频");
		video.setThumb(urlImage);

		UMusic uMusic = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
		uMusic.setAuthor("umeng");
		uMusic.setTitle("天籁之音");
		uMusic.setThumb(urlImage);
		// uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("----移动电子处方平台--正文内容....");
		weixinContent.setTitle("移动电子处方平台--标题内容");
		weixinContent.setTargetUrl("http://www.umeng.com");
		weixinContent.setShareMedia(localImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("----移动电子处方平台--正文内容....");
		circleMedia.setTitle("移动电子处方平台--标题内容");
		circleMedia.setShareImage(localImage);
//		 circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl("http://www.umeng.com");
		mController.setShareMedia(circleMedia);

		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent.setShareContent("人人分享内容");
		UMImage image = new UMImage(getActivity(), BitmapFactory.decodeResource(getResources(), R.drawable.device));
		image.setTitle("thumb title");
		image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");
		renrenShareContent.setShareImage(image);
		renrenShareContent.setAppWebSite("http://www.umeng.com/social");
		mController.setShareMedia(renrenShareContent);

		UMImage qzoneImage = new UMImage(getActivity(), "http://www.umeng.com/images/pic/social/integrated_3.png");
		qzoneImage.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
		qzone.setTargetUrl("http://www.umeng.com/social");
		qzone.setTitle("QZone title");
		qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);

		//        video.setThumb(new UMImage(getActivity(), BitmapFactory.decodeResource(
		//                getResources(), R.drawable.device)));

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("----移动电子处方平台--正文内容----朋友圈");
		qqShareContent.setTitle("hello, happy day!");
		qqShareContent.setShareImage(urlImage);
		// qqShareContent.setShareMusic(uMusic);
		// qqShareContent.setShareVideo(video);
		//        qqShareContent.setTargetUrl("http://www.umeng.com/social");
		qqShareContent.setTargetUrl("http://www.qq.com/a.apk");
		mController.setShareMedia(qqShareContent);

		// 视频分享
		UMVideo umVideo = new UMVideo("http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		umVideo.setTitle("友盟社会化组件视频");

		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
		// 设置tencent分享内容
		mController.setShareMedia(tencent);

		// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
		MailShareContent mail = new MailShareContent(localImage);
		mail.setTitle("share form umeng social sdk");
		mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，email");
		// 设置tencent分享内容
		mController.setShareMedia(mail);

		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，短信");
		sms.setShareImage(urlImage);
		mController.setShareMedia(sms);

		SinaShareContent sinaContent = new SinaShareContent(urlImage);
		sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，新浪微博");
		mController.setShareMedia(sinaContent);

		TwitterShareContent twitterShareContent = new TwitterShareContent();
		twitterShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，TWITTER");
		twitterShareContent.setShareMedia(localImage);
		mController.setShareMedia(twitterShareContent);

		GooglePlusShareContent googlePlusShareContent = new GooglePlusShareContent();
		googlePlusShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，G+");
		googlePlusShareContent.setShareMedia(localImage);
		mController.setShareMedia(googlePlusShareContent);
	}

	/**
	 * 添加所有的平台</br>
	 */
	private void addCustomPlatforms() {
		// 添加微信平台
		addWXPlatform();
		// 添加QQ平台
		addQQQZonePlatform();
		// 添加印象笔记平台
		addEverNote();
		// 添加facebook平台
		addFacebook();
		// 添加Instagram平台
		addInstagram();
		// 添加来往、来往动态平台
		addLaiWang();
		// 添加LinkedIn平台
		addLinkedIn();
		// 添加Pinterest平台
		addPinterest();
		// 添加Pocket平台
		addPocket();
		// 添加有道云平台
		addYNote();
		// 添加易信平台
		addYXPlatform();
		// 添加短信平台
		addSMS();
		// 添加email平台
		addEmail();

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN, SHARE_MEDIA.EMAIL,
				SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.LAIWANG,
				SHARE_MEDIA.LAIWANG_DYNAMIC, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.PINTEREST, SHARE_MEDIA.POCKET, SHARE_MEDIA.SMS,
				SHARE_MEDIA.TWITTER, SHARE_MEDIA.YIXIN, SHARE_MEDIA.YIXIN_CIRCLE, SHARE_MEDIA.YNOTE);
		mController.openShare(getActivity(), false);
	}

	/**
	 * 添加短信平台</br>
	 */
	private void addSMS() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	/**
	 * 添加Email平台</br>
	 */
	private void addEmail() {
		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	/**
	 * Pocket分享。pockect只支持分享网络链接</br>
	 */
	private void addPocket() {
		UMPocketHandler pocketHandler = new UMPocketHandler(getActivity());
		pocketHandler.addToSocialSDK();
		PocketShareContent pocketShareContent = new PocketShareContent();
		pocketShareContent.setShareContent("http://www.umeng.com/social");
		mController.setShareMedia(pocketShareContent);
	}

	/**
	 * LinkedIn分享。LinkedIn只支持图片，文本，图文分享</br>
	 */
	private void addLinkedIn() {
		UMLinkedInHandler linkedInHandler = new UMLinkedInHandler(getActivity());
		linkedInHandler.addToSocialSDK();
		LinkedInShareContent linkedInShareContent = new LinkedInShareContent();
		linkedInShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，LinkedIn");
		mController.setShareMedia(linkedInShareContent);
	}

	/**
	 * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享</br>
	 */
	private void addYNote() {
		UMYNoteHandler yNoteHandler = new UMYNoteHandler(getActivity());
		yNoteHandler.addToSocialSDK();
		YNoteShareContent yNoteShareContent = new YNoteShareContent();
		yNoteShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，云有道笔记");
		yNoteShareContent.setTitle("友盟分享组件");
		yNoteShareContent.setShareImage(new UMImage(getActivity(), new File("/storage/sdcard0/test12.png")));
		mController.setShareMedia(yNoteShareContent);
	}

	/**
	 * 添加印象笔记平台
	 */
	private void addEverNote() {
		UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(getActivity());
		evernoteHandler.addToSocialSDK();

		// 设置evernote的分享内容
		EvernoteShareContent shareContent = new EvernoteShareContent("印象笔记文本内容");
		shareContent.setShareMedia(new UMImage(getActivity(), R.drawable.device));
		mController.setShareMedia(shareContent);
	}

	/**
	 * 添加Pinterest平台
	 */
	private void addPinterest() {
		/**
		 * app id需到pinterest开发网站( https://developers.pinterest.com/ )自行申请.
		 */
		UMPinterestHandler pinterestHandler = new UMPinterestHandler(getActivity(), "1439206");
		pinterestHandler.addToSocialSDK();

		// 设置Pinterest的分享内容
		PinterestShareContent shareContent = new PinterestShareContent("Pinterest文本内容");
		shareContent.setShareMedia(new UMImage(getActivity(), R.drawable.device));
		mController.setShareMedia(shareContent);
	}

	/**
	 * 添加来往和来往动态平台</br>
	 */
	private void addLaiWang() {

		String appToken = "laiwangd497e70d4";
		String secretID = "d497e70d4c3e4efeab1381476bac4c5e";
		// laiwangd497e70d4:来往appToken,d497e70d4c3e4efeab1381476bac4c5e:来往secretID
		// 添加来往的支持
		UMLWHandler umlwHandler = new UMLWHandler(getActivity(), appToken, secretID);
		umlwHandler.addToSocialSDK();

		// 添加来往动态的支持
		UMLWHandler lwDynamicHandler = new UMLWHandler(getActivity(), appToken, secretID);
		lwDynamicHandler.setToCircle(true);
		lwDynamicHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx2927dce7f27084a3";
		String appSecret = "8d84c8ce7465be8c286516f1e4e0cc53";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "wx2927dce7f27084a3";
		String appKey = "8d84c8ce7465be8c286516f1e4e0cc53";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @Title: addYXPlatform
	 * @Description:
	 * @throws
	 */
	private void addYXPlatform() {

		// 添加易信平台
		UMYXHandler yixinHandler = new UMYXHandler(getActivity(), "yxc0614e80c9304c11b0391514d09f13bf");
		// 关闭分享时的等待Dialog
		yixinHandler.enableLoadingDialog(false);
		// 设置target Url, 必须以http或者https开头
		yixinHandler.setTargetUrl("http://www.umeng.com");
		yixinHandler.addToSocialSDK();

		// 易信朋友圈平台
		UMYXHandler yxCircleHandler = new UMYXHandler(getActivity(), "yxc0614e80c9304c11b0391514d09f13bf");
		yxCircleHandler.setToCircle(true);
		yxCircleHandler.addToSocialSDK();

	}

	/**
	 * @Title: addFacebook
	 * @Description:
	 * @throws
	 */
	private void addFacebook() {

		UMImage mUMImgBitmap = new UMImage(getActivity(), R.drawable.device);
		UMFacebookHandler mFacebookHandler = new UMFacebookHandler(getActivity(), "567261760019884", PostType.FEED);
		mFacebookHandler.addToSocialSDK();

		FaceBookShareContent fbContent = new FaceBookShareContent("facebook 分享组件");
		fbContent.setShareImage(mUMImgBitmap);
		fbContent.setShareContent("This is my facebook social share sdk." + new Date().toString());
		fbContent.setTitle("Title - FB");
		fbContent.setCaption("Caption - Fb");
		fbContent.setDescription("独立拆分测试");
		fbContent.setTargetUrl("http://www.umeng.com/");
		mController.setShareMedia(fbContent);

		mController.setShareContent("facebook share");
		mController.setShareMedia(mUMImgBitmap);

	}

	/**
	 * </br> Instagram只支持图片分享, 只支持纯图片分享.</br>
	 */
	private void addInstagram() {
		// 构建Instagram的Handler
		UMInstagramHandler instagramHandler = new UMInstagramHandler(getActivity());
		instagramHandler.addToSocialSDK();

		UMImage localImage = new UMImage(getActivity(), R.drawable.device);

		// // 添加分享到Instagram的内容
		InstagramShareContent instagramShareContent = new InstagramShareContent(localImage);
		mController.setShareMedia(instagramShareContent);
	}

}
