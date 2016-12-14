package com.zhuoyi.fauction.net;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Constant {
    public static String HOST="http://api.nongpaipai.cn/";
   //public static String HOST="http://test.nongpaipai.cn/";
    public static String PORT="82";
    public static String VERSION="/V5";
    //public static String VERSION="/api";
    public static String URL=HOST+VERSION;
    //用户安装注册app
    public static String installation=URL+"/Installation/registration";
    //引导员和品种接口
    public static String MAIN_INDEX=URL+"?kind=Main&method=index";
    //首页轮播图
    public static String MAIN_INDEXAD=URL+"?kind=Main&method=indexAd";
    //用户登录
    public static String MEMBER_LOGIN=URL+"?kind=Member&method=login";
    //用户注册协议
    public static String NEW_AGREEMENT=URL+"?kind=New&method=agreement";
    //注册
    public static String MEMBER_REG=URL+"?kind=Member&method=reg";
    //品种导航接口
    public static String MAIN_INDEXCATEGORY=URL+"?kind=Main&method=indexCategory";
    //正在进行
    public static String PRODUCT_DEAL=URL+"?kind=Product&method=deal";
    //首页滚动最新成交
    public static String MAIN_TURNOVER=URL+"?kind=Main&method=turnover";
    //最新成交
    public static String MAIN_DEAL=URL+"?kind=Main&method=deal";
    //拍卖预展
    public static String MAIN_PREVIEW=URL+"?kind=Main&method=preview";
    //捡漏专区
    public static String MAIN_DROPCATCHING=URL+"?kind=Main&method=dropCatching";
    //火热进行
    public static String MAIN_FAUCTION=URL+"?kind=Main&method=auction";
    //商品详情正在进行第一页
    public static String PRODUCT_VIEWING=URL+"?kind=Product&method=viewing";
    //商品详情第二页
    public static String PRODUCT_VIEWCONTENT=URL+"?kind=Product&method=viewContent";
    //商品详情报名出价纪录
    public static String PRODUCT_AUCTIONRECORD=URL+"?kind=Product&method=auctionRecord";
    //出价接口
    public static String PRODUCT_OFFER=URL+"?kind=Product&method=offer";
    //报名缴纳保证金
    public static String PRODUCT_PAYBOND=URL+"?kind=Product&method=payBond";
    //用户个人信息
    public static String MEMBER_USERINFO=URL+"?kind=Member&method=userInfo";
    //用户退出登录
    public static String MEMBER_LOGINOUT=URL+"?kind=Member&method=loginOut";
    //订单信息接口
    public static String ORDER_ORDERINFO=URL+"?kind=Order&method=orderInfo";
    //订单提交接口
    public static String ORDER_SUBMITORDER=URL+"?kind=Order&method=submitOrder";
    //订单包装数据请求接口
    public static String ORDER_PACKING=URL+"?kind=Order&method=packing";
    //添加地址
    public static String MEMBER_INCREASEADDRESS=URL+"?kind=Member&method=increaseAddress";
    //地址列表
    public static String MEMBER_GETLISTDATA=URL+"?kind=Member&method=getListData";
    //我拍到的
    public static String ORDER_ORDERLIST=URL+"?kind=Order&method=orderList";
    //我参拍的
    public static String ORDER_MYSHOOTING=URL+"?kind=Order&method=myShooting";
    //我的提醒
    public static String ORDER_MYMINDLIST=URL+"?kind=Order&method=myRemindList";
    //设置提醒
    public static String PRODUCT_REMIND=URL+"?kind=Product&method=remind";
    //取消提醒
    public static String PRODUCT_DELREMINDE=URL+"?kind=Product&method=delRemind";
    //出价纪录列表
    public static String PRODUCT_AUCTIONRECORDLIST=URL+"?kind=Product&method=auctionRecordList";
    //详情第一页即将开始
    public static String PRODUCT_VIEWSOON=URL+"?kind=Product&method=viewSoon";
    //详情第一页即将开始
    public static String PRODUCT_VIEWED=URL+"?kind=Product&method=viewed";
    //列表即将开始
    public static String PRODUCT_SOON=URL+"?kind=Product&method=soon";
    //列表即将开始
    public static String PRODUCT_ENDED=URL+"?kind=Product&method=ended";
    //短信验证码获取接口
    public static String MEMBER_SENDCODE=URL+"?kind=Member&method=sendCode";
    //忘记密码 修改密码
    public static String MEMBER_FORGOTPASSWORD=URL+"?kind=Member&method=forgotPassword";
    //用户名修改
    public static String MEMBE_EDITUSERNAME=URL+"?kind=Member&method=editUserName";
    //删除地址
    public static String MEMBER_DELADDRESS=URL+"?kind=Member&method=delAddress";
    //修改地址
    public static String MEMBER_EDITADDRESS=URL+"?kind=Member&method=editAddress";
    //设置默认地址
    public static String MEMBER_SETADDRESS=URL+"?kind=Member&method=setAddress";
    //确认收货
    public static String ORDER_CONFIRMRECEIPT=URL+"?kind=Order&method=confirmReceipt";
    //删除订单
    public static String ORDER_DELORDER=URL+"?kind=Order&method=delOrder";
    //支付宝数据请求接口
    public static String PAY_ZFBPAY=URL+"?kind=Pay&method=zfbPay";
    //成功支付
    public static String ORDER_PAYRESULT=URL+"?kind=Order&method=payResult";
    //推荐接口
    public static String ORDER_RECOMMEND=URL+"?kind=Order&method=recommend";
    //首页推荐广告
    public static String MAIN_RECOMMENDAD=URL+"?kind=Main&method=recommendAd";
    //订单详情
    public static String ORDER_VIEWORDER=URL+"?kind=Order&method=viewOrder";
    //取消提醒
    public static String PRODUCT_DELREMIND=URL+"?kind=Product&method=delRemind";
    //获取拍卖状态
    public static String PRODUCT_GETSHOOTSTATUS=URL+"?kind=Product&method=getShootStatus";
//    //首页最新成交
//    public static String MAIN_TURNOVER=URL+"?kind=Main&method=turnover";
    //钱包信息
    public static String WALLET_INDEX=URL+"?kind=Wallet&method=index";
    //添加账户信息
    public static String WALLET_ACCOUNT=URL+"?kind=Wallet&method=addAccount";
    //账户信息列表
    public static String WALLET_ACCOUNTLIST=URL+"?kind=Wallet&method=accountList";
    //删除账户信息
    public static String WALLET_DELACCOUNT=URL+"?kind=Wallet&method=delAccount";
    //申请提现
    public static String WALLET_WITHDRAWALS=URL+"?kind=Wallet&method=withdrawals";
    //充值
    public static String WALLET_RECHARGE=URL+"?kind=Wallet&method=recharge";
    //获取头像token
    public static String MEMBER_GETUPTOKEN=URL+"?kind=Member&method=getUpToken";
    //头像保存接口
    public static String MEMBER_SAVEPICTURE=URL+"?kind=Member&method=savePicture";
    //钱包明细
    public static String WALLET_INFO=URL+"?kind=Wallet&method=info";
    //保证金明细
    public static String ORDER_BONDINFO=URL+"?kind=Order&method=bondInfo";
    //网银支付TN获取接口
    public static String PAY_WYZF=URL+"?kind=Pay&method=wyzf";

    //开放环境前端消息推送接口
    //public static String FRONT_PUSH="http://114.55.88.233:2256";
     //正式环境前端消息推送接口
    public static String FRONT_PUSH="http://114.55.88.233:2120";


   //获取帅选联动菜单数据
   public static String CO_GETBASE=URL+"?kind=Co&method=getBase";

    //获取合作基地列表数据
    public static String CO_LISTDATE=URL+"?kind=Co&method=listDate";

   //获取合作基地内容数据
   public static String CO_VIEW=URL+"?kind=Co&method=view";

    //线下支付提交接口
    public static String ORDER_OFFINEPAYMENT=URL+"?kind=Order&method=offlinePayment";

    //线下支付供应商信息接口
    public static String CO_GETGYSINFO=URL+"?kind=Co&method=getGysInfo";

    //我的抵用劵
    public static String VOUCHER_GETVOUCHER=URL+"?kind=Voucher&method=getVoucher";

    //获取抵用劵
    public static String VOUCHER_USEVOUCHER=URL+"?kind=Voucher&method=useVoucher";

    //订单钱包支付及订单金额为0的提交接口
    public static String WALLET_PAYORDER=URL+"?kind=Wallet&method=payOrder";

    //保证金钱在线支付使用优惠券提交接口
    public static String VOUCHER_PAYVOUCHER=URL+"?kind=Voucher&method=payVoucher";

    //保证金钱包支付及保证金为0的提交接口
    public static String WALLET_PAYBOND=URL+"?kind=Wallet&method=payBond";

 //商品详情
 public static String PRODUCT_DETAIL=URL+"/new/view/id/";

 //
 public static String PRODUCT_KNOW=URL+"/new/index/id/";

 //拍卖协议
 public static String XIEYI=URL+"/new/index/id/2";
 //抵用券使用说明
 public static String PICKET_HELP=URL+"/new/index/id/6";
 //注册协议
 public static String REGIST_XIEYI=URL+"/new/index/id/1";
 //联系我们
 public static String CONTACTS_US="http://api.nongpaipai.cn/lxwm/";
}
