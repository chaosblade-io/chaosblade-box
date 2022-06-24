(self.webpackChunk=self.webpackChunk||[]).push([[57],{91714:function(f,o,e){"use strict";var l=this&&this.__assign||function(){return l=Object.assign||function(r){for(var n,i=1,y=arguments.length;i<y;i++){n=arguments[i];for(var t in n)Object.prototype.hasOwnProperty.call(n,t)&&(r[t]=n[t])}return r},l.apply(this,arguments)},u=this&&this.__importDefault||function(r){return r&&r.__esModule?r:{default:r}};Object.defineProperty(o,"__esModule",{value:!0});var s=u(e(27378)),c=e(30156),d=u(e(55839)),p=e(46949),I=function(r){return s.default.createElement(c.Step,l({stretch:!0},r))};d.default(I,c.Step),o.default=p.withThemeClass(I)},39257:function(f,o,e){var l,u,s,c=e(67394);(function(d,p){if(!0)!(u=[o,e(91714),e(17225),e(17534),e(27378),e(66697),e(98784),e(36012),e(14798),e(16818),e(99328),e(42058)],l=p,s=typeof l=="function"?l.apply(o,u):l,s!==void 0&&(f.exports=s));else var I})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(d,p,I,r,n,i,y,t,_,a,U,m){"use strict";var E=e(67971);c(d,"__esModule",{value:!0}),d.default=void 0,p=E(p),I=E(I),r=E(r),n=E(n),i=E(i),y=E(y),t=E(t),_=E(_),a=E(a);function j(w){var z=w.way,D=w.language,Q=(0,m.useHistory)();function A(){var x=document.getElementById("code").innerText;(0,t.default)(x)?r.default.success(_.default.t("Copy successfully")):r.default.error(_.default.t("Replication failed"))}function v(x){return n.default.createElement("div",{className:a.default.codeContent},n.default.createElement("span",{id:"code"},x.map(function(h){return n.default.createElement("div",{className:a.default.codeLine},h)})),n.default.createElement("div",{className:a.default.copy,onClick:A},n.default.createElement(I.default,{type:"copy",className:a.default.copyIcon})))}function C(){return n.default.createElement("div",null,n.default.createElement("p",null,n.default.createElement(i.default,null,"AppName can only contain letters, numbers and special characters _-")),v([_.default.t("-Dproject.name=application name -Dproject.group.name=application group name")]),n.default.createElement("div",{className:a.default.jvmParam},n.default.createElement(i.default,null,"Please replace the above values according to the actual situation. The default values of the above configuration are as follows"),": "),n.default.createElement("ul",{className:a.default.ulList},n.default.createElement("li",null,"project.name: ",n.default.createElement(i.default,null,"Defaults value")," chaos-default-app"),n.default.createElement("li",null,"project.group.name: ",n.default.createElement(i.default,null,"Defaults value")," chaos-default-app-group")),n.default.createElement(r.default,{title:"",type:"warning",className:a.default.jvmWaring},n.default.createElement(i.default,null,'   "Note: On the machine where the fault rehearsal probe has been deployed, modify the JVM startup parameters and restart, the application will automatically recognize and take effect without redeploying the fault rehearsal probe"')))}function g(){return n.default.createElement("div",null,n.default.createElement("p",null,n.default.createElement(i.default,null,"The value of the tag can only contain letters, numbers and special characters_-")),n.default.createElement("div",{className:a.default.podWord},n.default.createElement(i.default,null,"In the template (YAML format) add the following labels configuration to the spec > template < labels level"),": "),v(["labels:","chaos/app-instance: ".concat(_.default.t("ApplicationName")),"chaos/app-group: ".concat(_.default.t("Application group name"))]),n.default.createElement("div",{className:a.default.podWord},n.default.createElement(i.default,null,"Replace the above values according to the actual situation. If you do not configure the above values, it will be recognized again whether it contains"),n.default.createElement("span",{className:a.default.tag},"app-group-name(",n.default.createElement(i.default,null,"Container Service Application Configuration"),")"),"\u3001",n.default.createElement("span",{className:a.default.tag},"app.kubernetes.io/name"),"\u3001",n.default.createElement("span",{className:a.default.tag},"app"),"\u3001",n.default.createElement("span",{className:a.default.tag},"k8s-app"),n.default.createElement(i.default,null,"The label configuration is used as the application name, and the default application group name format is"),": ",n.default.createElement("span",{className:a.default.nameStyle},n.default.createElement(i.default,null,"Application name-group")),"\u3002"),n.default.createElement("div",{className:a.default.podWord,style:{margin:"14px 0"}},n.default.createElement(i.default,null,"According to the label identification application, the label priority is as follows"),": chaos/app-instance ",">"," app-group-name ",">"," app.kubernetes.io/name ",">"," k8s-app ",">"," app\u3002"),n.default.createElement("div",{className:a.default.podWord},n.default.createElement("span",{className:a.default.nameStyle},n.default.createElement(i.default,null,"Notice"),": "),n.default.createElement(i.default,null,"The default application attribution is no longer supported in kubernetes mode, please follow the prompts above to configure the label")))}function W(x){(0,U.pushUrl)(Q,"/manage/setting",x)}function b(){var x;return z==="k8s"?x=[{title:_.default.t("Configure Pod Labels"),content:g()},{title:_.default.t("Use the chart package to automatically install fault drill probes"),content:n.default.createElement("div",{className:a.default.imageContent},n.default.createElement("img",{src:e(67421)}))}]:D==="java"?x=[{title:_.default.t("Configure JVM startup parameters"),content:C()},{title:_.default.t("Just start the app"),content:""},{title:n.default.createElement("span",null,n.default.createElement(i.default,null,"Enter the probe management page, click"),n.default.createElement("a",{onClick:function(){return W({region:"public"})},target:"_black"},n.default.createElement(i.default,null,"Install the Troubleshooting Probe"))),content:n.default.createElement("div",{className:a.default.imageContent},n.default.createElement("img",{src:e(30158)}))},{title:_.default.t("Fill in the application, application group information, IP, ssh user, and ssh password. If you already have an application, select the application name and application group, and click Install"),content:n.default.createElement("div",{className:a.default.imageContent},n.default.createElement("img",{src:e(12561)}))}]:x=[{title:n.default.createElement("span",null,n.default.createElement(i.default,null,"Enter the probe management page, click"),n.default.createElement("a",{onClick:function(){return W({})},target:"_black"},n.default.createElement(i.default,null,"Install the Troubleshooting Probe"))),content:n.default.createElement("div",{className:a.default.imageContent},n.default.createElement("img",{src:e(30158)}))},{title:_.default.t("Fill in the application, application group information, IP, ssh user, and ssh password. If you already have an application, select the application name and application group, and click Install"),content:n.default.createElement("div",{className:a.default.imageContent},n.default.createElement("img",{src:e(12561)}))}],y.default.map(x,function(h,R){return n.default.createElement(p.default.Item,{key:R,title:h.title,content:h.content})})}return n.default.createElement("div",{className:a.default.stepContent},n.default.createElement(p.default,{direction:"ver",shape:"circle",animation:!1,readOnly:!0},b()))}var P=j;d.default=P})},69057:function(f,o,e){var l,u,s,c=e(24596),d=e(67394),p=e(93168),I=e(23587);(function(r,n){if(!0)!(u=[o,e(17534),e(81853),e(39257),e(27378),e(66697),e(60042),e(14798),e(78571),e(96291),e(14870)],l=n,s=typeof l=="function"?l.apply(o,u):l,s!==void 0&&(f.exports=s));else var i})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(r,n,i,y,t,_,a,U,m,E,j){"use strict";var P=e(67971);d(r,"__esModule",{value:!0}),r.default=void 0,n=P(n),i=P(i),y=P(y),t=z(t),_=P(_),a=P(a),U=P(U),m=P(m);function w(A){if(typeof p!="function")return null;var v=new p,C=new p;return(w=function(W){return W?C:v})(A)}function z(A,v){if(!v&&A&&A.__esModule)return A;if(A===null||c(A)!=="object"&&typeof A!="function")return{default:A};var C=w(v);if(C&&C.has(A))return C.get(A);var g={},W=d&&I;for(var b in A)if(b!=="default"&&Object.prototype.hasOwnProperty.call(A,b)){var x=W?I(A,b):null;x&&(x.get||x.set)?d(g,b,x):g[b]=A[b]}return g.default=A,C&&C.set(A,g),g}function D(){var A=(0,j.useDispatch)(),v=(0,t.useState)("host"),C=(0,i.default)(v,2),g=C[0],W=C[1],b=(0,t.useState)("java"),x=(0,i.default)(b,2),h=x[0],R=x[1];(0,t.useEffect)(function(){A.pageHeader.setTitle(t.default.createElement(_.default,null,"Application access")),A.pageHeader.setBreadCrumbItems(E.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"application",value:U.default.t("Application management"),path:"/chaos/application"},{key:"freshapplication_access",value:U.default.t("Application access"),path:"/chaos/freshapplication/access"}]))},[]);function N(B,M){B==="way"?(W(M),R("java")):B==="language"&&R(M)}function T(B,M,F,O,V){var S,L,X=M;return(B===g||B===h)&&(S=m.default.chioseCard,L=m.default.chioseName,X=F),t.default.createElement("div",{className:(0,a.default)(m.default.card,S),key:B,onClick:function(){return N(V,B)}},t.default.createElement("img",{src:X,className:m.default.img}),t.default.createElement("div",{className:(0,a.default)(m.default.name,L)},O))}function k(B,M,F){return t.default.createElement("div",{className:m.default.cardContent},T(B[0],B[1],B[2],B[3],F),T(M[0],M[1],M[2],M[3],F))}function Y(){return t.default.createElement("div",null,t.default.createElement("div",{className:m.default.contentChiose},t.default.createElement("div",{className:m.default.title},t.default.createElement(_.default,null,"Application management, please select application language")),k(["java","https://img.alicdn.com/tfs/TB18mMPJ7L0gK0jSZFtXXXQCXXa-24-32.png","https://img.alicdn.com/tfs/TB1gSMWJ7T2gK0jSZFkXXcIQFXa-24-32.png","Java"],["other","https://img.alicdn.com/tfs/TB1OT.TJ.Y1gK0jSZFCXXcwqXXa-26-30.png","https://img.alicdn.com/tfs/TB18U4dbP39YK4jSZPcXXXrUFXa-26-30.png","\u5176\u5B83"],"language")),h==="java"&&t.default.createElement("div",{className:m.default.guide},t.default.createElement(_.default,null,"Specify the application and application group by configuring the JVM startup parameters, which is used to accurately divide the application / application group to which the machine belongs (it does not conflict with the application / application group specified when installing the probe). The configuration steps are as follows:")),t.default.createElement(y.default,{way:g,language:h}))}return t.default.createElement("div",{className:m.default.appAccess},t.default.createElement("div",{className:m.default.contentChiose},t.default.createElement("div",{className:m.default.title},t.default.createElement(_.default,null,"Please select application deployment method")),k(["host","https://img.alicdn.com/tfs/TB1TV0WaDM11u4jSZPxXXahcXXa-28-28.png","https://img.alicdn.com/tfs/TB15tsRJ.Y1gK0jSZFMXXaWcVXa-28-28.png","host"],["k8s","https://img.alicdn.com/tfs/TB1T5UQJVP7gK0jSZFjXXc5aXXa-30-30.png","https://img.alicdn.com/tfs/TB1k7J4fycKOu4jSZKbXXc19XXa-30-30.png","Kubernetes"],"way")),g==="host"?Y():t.default.createElement("div",null,t.default.createElement("div",{className:m.default.guide},t.default.createElement(_.default,null,"Identify the application / application group to which it belongs through the pod tag. The configuration is as follows:")),t.default.createElement(y.default,{way:g,language:h}),t.default.createElement(n.default,{title:"",type:"warning",className:m.default.jvmWaring},t.default.createElement(_.default,null,"In the cluster where the fault drill probe has been deployed, modifying the pod tag can take effect without redeploying the fault drill probe."))))}var Q=D;r.default=Q})},10975:(f,o,e)=>{"use strict";e.d(o,{Z:()=>p});var l=e(60994),u=e.n(l),s=e(93476),c=e.n(s),d=c()(u());d.push([f.id,`  .index__codeContent__U53LG {
    width: 100%;
    padding: 16px 0 16px 24px;
    background: #F2F4F5;
    font-size: 12px;
    color: #333333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    position: relative;
}

.index__copy__YQczU {
      cursor: pointer;
      width: 48px;
      height: 36px;
      background: #dedede;
      padding: 10px 17px;
      position: absolute;
      top: 0;
      right: 0;
    }

.index__copy__YQczU .index__copyIcon__2gTPa {
        width: 14px !important;
        height: 16px !important;
      }

.index__copy__YQczU .index__copyIcon__2gTPa::before{
          width: 14px !important;
          height: 16px !important;
          color: #fff;
        }

  .index__codeLine__TDTbc {
    line-height: 20px;
  }

  .index__jvmParam__lQsfu {
    font-size: 12px;
    color: #333333;
    font-family: PingFangSC-Medium;
    margin-bottom: 6px;
  }
  .index__jvmWaring__bBbW\\+ {
    margin-top: 16px;
  }

  .index__podWord__a6Eha {
    font-size: 12px;
    color: #333333;
    line-height: 22px;
  }

  .index__podWord__a6Eha .index__tag__tPkcQ {
      display: inline-block;
      height: 20px;
      padding: 0 8px;
      color: #333;
      background: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 2px;
      -webkit-border-radius: 2px;
      -moz-border-radius: 2px;
      -ms-border-radius: 2px;
      -o-border-radius: 2px;
      margin: 0 2px;
    }

  .index__podWord__a6Eha .index__nameStyle__W5eSA {
      font-family: PingFangSC-Medium;
      font-size: 12px;
      color: #333333;
    }

  .index__imageContent__iH4Zj {
    width: 960px;
    min-height: 154px;
  }

  .index__imageContent__iH4Zj img {
      width: 100%;
      height: 100%;
    }

  .index__altWord__VheTV {
    font-size: 12px;
    color: #555;
    line-height: 24px;
  }

  .index__guide__wKpbn {
    font-size: 12px;
    color: #333;
    margin-top: 24px;
    font-family: PingFangSC-Regular;
  }

.index__ulList__nGFf9 {
    margin-top: 6px;
  }

.index__ulList__nGFf9 li {
      line-height: 22px;
    }

.index__ulList__nGFf9 li:before {
        content: "";
        width: 4px;
        height: 4px;
        display: inline-block;
        border-radius: 50%;
        background: #0070cc;
        vertical-align: middle;
        margin-right: 6px;
        }


.index__stepContent__isqPV {
    margin-top: 24px; 
  }


.index__stepContent__isqPV .next-step-item-wait .next-step-item-container .next-step-item-node-placeholder .next-step-item-node .next-step-item-node-circle {
      background: #0070cc;
      border-color: #0070cc;
      color: #fff;
    }


.index__stepContent__isqPV .next-step-item-wait .next-step-item-body .next-step-item-title {
      color: #333;
    }`,"",{version:3,sources:["webpack://./pages/Chaos/Application/AccessStepComponent/index.css"],names:[],mappings:"EAAE;IACE,WAAW;IACX,yBAAyB;IACzB,mBAAmB;IACnB,eAAe;IACf,cAAc;IACd,aAAa;IACb,8BAA8B;IAC9B,mBAAmB;IACnB,mBAAmB;IACnB,kBAAkB;AACtB;;AAEA;MACM,eAAe;MACf,WAAW;MACX,YAAY;MACZ,mBAAmB;MACnB,kBAAkB;MAClB,kBAAkB;MAClB,MAAM;MACN,QAAQ;IAYV;;AAVE;QACE,sBAAsB;QACtB,uBAAuB;MAOzB;;AALE;UACE,sBAAsB;UACtB,uBAAuB;UACvB,WAAW;QACb;;EAIN;IACE,iBAAiB;EACnB;;EAEA;IACE,eAAe;IACf,cAAc;IACd,8BAA8B;IAC9B,kBAAkB;EACpB;EACA;IACE,gBAAgB;EAClB;;EAEA;IACE,eAAe;IACf,cAAc;IACd,iBAAiB;EAsBnB;;EApBE;MACE,qBAAqB;MACrB,YAAY;MACZ,cAAc;MACd,WAAW;MACX,mBAAmB;MACnB,sBAAsB;MACtB,kBAAkB;MAClB,0BAA0B;MAC1B,uBAAuB;MACvB,sBAAsB;MACtB,qBAAqB;MACrB,aAAa;IACf;;EAEA;MACE,8BAA8B;MAC9B,eAAe;MACf,cAAc;IAChB;;EAGF;IACE,YAAY;IACZ,iBAAiB;EAMnB;;EAJE;MACE,WAAW;MACX,YAAY;IACd;;EAGF;IACE,eAAe;IACf,WAAW;IACX,iBAAiB;EACnB;;EAEA;IACE,eAAe;IACf,WAAW;IACX,gBAAgB;IAChB,+BAA+B;EACjC;;AAEF;IACI,eAAe;EAejB;;AAdE;MACE,iBAAiB;IAYnB;;AAVE;QACE,WAAW;QACX,UAAU;QACV,WAAW;QACX,qBAAqB;QACrB,kBAAkB;QAClB,mBAAmB;QACnB,sBAAsB;QACtB,iBAAiB;QACjB;;;AAKR;IACI,gBAAgB;EAWlB;;;AATE;MACE,mBAAmB;MACnB,qBAAqB;MACrB,WAAW;IACb;;;AAEA;MACE,WAAW;IACb",sourcesContent:[`  .codeContent {
    width: 100%;
    padding: 16px 0 16px 24px;
    background: #F2F4F5;
    font-size: 12px;
    color: #333333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    position: relative;
}

.copy {
      cursor: pointer;
      width: 48px;
      height: 36px;
      background: #dedede;
      padding: 10px 17px;
      position: absolute;
      top: 0;
      right: 0;

      .copyIcon {
        width: 14px !important;
        height: 16px !important;

        &::before{
          width: 14px !important;
          height: 16px !important;
          color: #fff;
        }
      }
    }

  .codeLine {
    line-height: 20px;
  }

  .jvmParam {
    font-size: 12px;
    color: #333333;
    font-family: PingFangSC-Medium;
    margin-bottom: 6px;
  }
  .jvmWaring {
    margin-top: 16px;
  }

  .podWord {
    font-size: 12px;
    color: #333333;
    line-height: 22px;

    .tag {
      display: inline-block;
      height: 20px;
      padding: 0 8px;
      color: #333;
      background: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 2px;
      -webkit-border-radius: 2px;
      -moz-border-radius: 2px;
      -ms-border-radius: 2px;
      -o-border-radius: 2px;
      margin: 0 2px;
    }

    .nameStyle {
      font-family: PingFangSC-Medium;
      font-size: 12px;
      color: #333333;
    }
  }

  .imageContent {
    width: 960px;
    min-height: 154px;

    img {
      width: 100%;
      height: 100%;
    }
  }

  .altWord {
    font-size: 12px;
    color: #555;
    line-height: 24px;
  }

  .guide {
    font-size: 12px;
    color: #333;
    margin-top: 24px;
    font-family: PingFangSC-Regular;
  }

.ulList {
    margin-top: 6px;
    li {
      line-height: 22px;

      &:before {
        content: "";
        width: 4px;
        height: 4px;
        display: inline-block;
        border-radius: 50%;
        background: #0070cc;
        vertical-align: middle;
        margin-right: 6px;
        }
    }
  }


.stepContent {
    margin-top: 24px;

    :global(.next-step-item-wait .next-step-item-container .next-step-item-node-placeholder .next-step-item-node .next-step-item-node-circle) {
      background: #0070cc;
      border-color: #0070cc;
      color: #fff;
    } 

    :global(.next-step-item-wait .next-step-item-body .next-step-item-title) {
      color: #333;
    } 
  }`],sourceRoot:""}]),d.locals={codeContent:"index__codeContent__U53LG",copy:"index__copy__YQczU",copyIcon:"index__copyIcon__2gTPa",codeLine:"index__codeLine__TDTbc",jvmParam:"index__jvmParam__lQsfu",jvmWaring:"index__jvmWaring__bBbW+",podWord:"index__podWord__a6Eha",tag:"index__tag__tPkcQ",nameStyle:"index__nameStyle__W5eSA",imageContent:"index__imageContent__iH4Zj",altWord:"index__altWord__VheTV",guide:"index__guide__wKpbn",ulList:"index__ulList__nGFf9",stepContent:"index__stepContent__isqPV"};const p=d},29316:(f,o,e)=>{"use strict";e.d(o,{Z:()=>p});var l=e(60994),u=e.n(l),s=e(93476),c=e.n(s),d=c()(u());d.push([f.id,`.index__warp__TrTm0 .index__cardContent__mLHmz {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
  }
  .index__warp__TrTm0 .index__pagination__xwPyo {
    text-align: right;
    margin-top: 16px;
  }
  .index__warp__TrTm0 .index__emptyData__xV52H {
    display: flex;
    align-items: center;
    justify-self: start;
    margin: auto;
    margin-top: 10%;
  }
  .index__warp__TrTm0 .index__emptyData__xV52H img {
      width: 137px;
      height: 130px;
      margin-right: 30px;
    }
  .index__warp__TrTm0 .index__emptyData__xV52H div .index__title__BqeBz {
        font-family: PingFangSC-Medium;
        font-size: 18px;
        color: #222222;
        margin-bottom: 8px;
      }
  .index__warp__TrTm0 .index__emptyData__xV52H div .index__hrefAction__pC7Qd {
        font-family: PingFangSC-Regular;
        font-size: 12px;
        color: #0070CC;
        cursor: pointer;
      }
  .index__warp__TrTm0 .index__emptyData__xV52H div div {
        line-height: 20px;
      }

.index__card__wphgI {
  width: 384px;
  height: 136px;
  border: 1px solid #dedede;
  padding: 14px 16px 16px;
  margin-top: 16px;
  position: relative;
  cursor: pointer;
  margin-right: 16px;

}

.index__card__wphgI:hover {
    border: 1px solid rgba(0,112,204,0.36);
    box-shadow: 0 1px 8px 0 rgba(0,112,204,0.36);
  }

.index__card__wphgI .index__defaultIcon__nebiS {
    width: 28px;
    height: 28px;
    position: absolute;
    top: 0px;
    right: 0px;
  }

.index__card__wphgI .index__topContent__XOQUb {
    display: flex;
    justify-content: space-between;
    margin-bottom: 27px;
    line-height: 22px;
  }

.index__card__wphgI .index__topContent__XOQUb .index__cardTitle__LIfnN {
      font-size: 14px;
      color: #333;
      width: 205px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space:nowrap;
    }

.index__card__wphgI .index__topContent__XOQUb .index__cardTitle__LIfnN .index__tipIcon__8c1JC {
        margin-left: 8px;
      }

.index__card__wphgI .index__topContent__XOQUb .index__cardTitle__LIfnN .index__tipIcon__8c1JC i {
          font-size: 16px;
        }

.index__card__wphgI .index__topContent__XOQUb .index__cardTitle__LIfnN .index__tipIcon__8c1JC i::before {
            width: 16px;
            height: 16px;
            font-size: 16px;
            vertical-align: inherit;
          }

.index__card__wphgI .index__topContent__XOQUb .index__typeTip__vBJEO {
      font-size: 12px;
      color: #c1c1c1;
    }

.index__card__wphgI .index__bottomContent__tpr9a {
    display: flex;
    justify-content: space-between;
  }

.index__card__wphgI .index__bottomContent__tpr9a .index__item__DdRxj .index__label__slw\\+k {
        font-size: 12px;
        color: #555;
        margin-bottom: 4px;
      }

.index__card__wphgI .index__bottomContent__tpr9a .index__item__DdRxj .index__value__MEW8U {
        font-size: 28px;
        color: #333;

      }

.index__actionContent__G0HXA {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.index__actionContent__G0HXA .index__searchContent__Y4lcx {
    width: 460px;
    margin-bottom: 8px;
  }

.index__appBase__uDJKb {
  width: 100%;
  height: 132px;
  border: 1px solid #dedede;
  padding: 16px;

}

.index__appBase__uDJKb .index__baseTitle__571Bf {
    font-size: 14px;
    color: #333;
    margin-bottom: 24px;
  }

.index__appBase__uDJKb .index__content__mmGfW {
    display: flex;
    justify-content: space-between;
  }

.index__appBase__uDJKb .index__content__mmGfW .index__leftContent__hZ2Jh .index__topLine__RZ8sR {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
        line-height: 20px;
      }

.index__appBase__uDJKb .index__content__mmGfW .index__leftContent__hZ2Jh .index__bottomLine__t6LZw {
        display: flex;
        justify-content: flex-start;
        line-height: 20px;
      }

.index__appBase__uDJKb .index__content__mmGfW .index__leftContent__hZ2Jh .index__lineItem__a9S7Q .index__lineLabel__rFDDI {
          font-size: 12px;
          color: #888;
          display: inline-block;
          width: 108px;
        }

.index__appBase__uDJKb .index__content__mmGfW .index__leftContent__hZ2Jh .index__lineItem__a9S7Q .index__lineValue__xUi3Q {
          font-size: 12px;
          color: #333;
          display: inline-block;
          width: 181px;
          margin-left: 8px;
        }

.index__appBase__uDJKb .index__content__mmGfW .index__rightContent__Z1N5a {
      display: flex;
      flex: 1;
      justify-content: space-around;
    }

.index__appBase__uDJKb .index__content__mmGfW .index__rightContent__Z1N5a .index__label__slw\\+k {
        font-size: 12px;
        color: #555;
        margin-bottom: 14px;
      }

.index__appBase__uDJKb .index__content__mmGfW .index__rightContent__Z1N5a .index__value__MEW8U {
        font-size: 28px;
        color: #333;
      }

.index__appBase__uDJKb .index__content__mmGfW .index__rightContent__Z1N5a .index__groupItem__bQvEA {
        width: 272px;
      }

.index__unit__n84gK {
  margin-left: 8px;
  font-size: 12px;
  color: #888;
}

.index__moreTag__TDApc {
  font-size: 12px;
  color: #0070cc;
  cursor: pointer;
}

.index__icon__m1SVF {
  font-size: 14px;
  margin-right: 8px;
}

.index__icon__m1SVF::before{
    font-size: 14px !important;
    width: 14px !important;
  }

.index__onLineState__3TH3R {
  color: #1E8E3E;
}

.index__notInstall__x9rH6 {
  color: #888;
}

.index__offLineState__HlF9j {
  color: #D93026;
}

.index__interrupt__zzMsc {
  color: #FFC440;
}

.index__loading__ieVUv {
  color: #888;
}

.index__appAccess__FPVdH {
  font-family: PingFangSC-Regular;
}

.index__appAccess__FPVdH .index__title__BqeBz {
    font-size: 14px;
    color: #333;
  }

.index__appAccess__FPVdH .index__contentChiose__5dGbe {
    margin-top: 20px;
  }

.index__appAccess__FPVdH .index__cardContent__mLHmz {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

.index__appAccess__FPVdH .index__card__wphgI {
    width: 180px;
    height: 72px;
    background: #F7F9FF;
    border: 1px solid #dedede;
    padding-top: 20px;
    padding-left: 25px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-right: 16px;
  }

.index__appAccess__FPVdH .index__card__wphgI .index__img__gmZIV {
      width: 28px;
      height: 28px;
      margin-right: 10px;
    }

.index__appAccess__FPVdH .index__card__wphgI .index__name__foEnk {
      font-size: 16px;
      color: #555;
    }

.index__appAccess__FPVdH .index__chioseCard__TviQG {
    background: #F3FAFF;
    border: 1px solid #0070CC;
    color: #0070CC;
  }

.index__appAccess__FPVdH .index__chioseCard__TviQG .index__chioseName__OY437 {
      color: #0070cc;
    }

.index__appAccess__FPVdH .index__stepContent__gtqNI {
    margin-top: 24px; 
  }

.index__appAccess__FPVdH .index__stepContent__gtqNI .next-step-item-wait .next-step-item-container .next-step-item-node-placeholder .next-step-item-node .next-step-item-node-circle {
      background: #0070cc;
      border-color: #0070cc;
      color: #fff;
    }

.index__appAccess__FPVdH .index__stepContent__gtqNI .next-step-item-wait .next-step-item-body .next-step-item-title {
      color: #333;
    }

.index__appAccess__FPVdH .index__codeContent__yzU75 {
    width: 100%;
    padding: 16px 0 16px 24px;
    background: #F2F4F5;
    font-size: 12px;
    color: #333333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    position: relative;
  }

.index__appAccess__FPVdH .index__codeContent__yzU75 .index__copy__wQghF {
      cursor: pointer;
      width: 48px;
      height: 36px;
      background: #dedede;
      padding: 10px 17px;
      position: absolute;
      top: 0;
      right: 0;
    }

.index__appAccess__FPVdH .index__codeContent__yzU75 .index__copy__wQghF .index__copyIcon__Cf84d {
        width: 14px !important;
        height: 16px !important;
      }

.index__appAccess__FPVdH .index__codeContent__yzU75 .index__copy__wQghF .index__copyIcon__Cf84d::before{
          width: 14px !important;
          height: 16px !important;
          color: #fff;
        }

.index__appAccess__FPVdH .index__codeLine__a1thc {
    line-height: 20px;
  }

.index__appAccess__FPVdH .index__jvmParam__FtgLJ {
    font-size: 12px;
    color: #333333;
    font-family: PingFangSC-Medium;
    margin-bottom: 6px;
  }

.index__appAccess__FPVdH .index__ulList__ZhGD3 {
    margin-top: 6px;
  }

.index__appAccess__FPVdH .index__ulList__ZhGD3 li {
      line-height: 22px;
    }

.index__appAccess__FPVdH .index__ulList__ZhGD3 li:before {
        content: "";
        width: 4px;
        height: 4px;
        display: inline-block;
        border-radius: 50%;
        background: #0070cc;
        vertical-align: middle;
        margin-right: 6px;
        }

.index__appAccess__FPVdH .index__jvmWaring__UhCGO {
    margin-top: 16px;
  }

.index__appAccess__FPVdH .index__podWord__Rji3T {
    font-size: 12px;
    color: #333333;
    line-height: 22px;
  }

.index__appAccess__FPVdH .index__podWord__Rji3T .index__tag__t0aBK {
      display: inline-block;
      height: 20px;
      padding: 0 8px;
      color: #333;
      background: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 2px;
      -webkit-border-radius: 2px;
      -moz-border-radius: 2px;
      -ms-border-radius: 2px;
      -o-border-radius: 2px;
      margin: 0 2px;
    }

.index__appAccess__FPVdH .index__podWord__Rji3T .index__nameStyle__eeJ-1 {
      font-family: PingFangSC-Medium;
      font-size: 12px;
      color: #333333;
    }

.index__appAccess__FPVdH .index__imageContent__QeJ0A {
    width: 960px;
    min-height: 154px;
  }

.index__appAccess__FPVdH .index__imageContent__QeJ0A img {
      width: 100%;
      height: 100%;
    }

.index__appAccess__FPVdH .index__altWord__8m9mu {
    font-size: 12px;
    color: #555;
    line-height: 24px;
  }

.index__appAccess__FPVdH .index__guide__teIpm {
    font-size: 12px;
    color: #333;
    margin-top: 24px;
    font-family: PingFangSC-Regular;
  }

.index__loading__ieVUv {
  padding: 15% 45%;
}

.index__nodeTags__TUos\\+ {
  margin-right: 4px;
  margin-bottom: 2px;
}

.index__setItem__zDdi\\+ {
  margin-bottom: 4px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  line-height: 28px;
}

.index__setItem__zDdi\\+ .index__label__slw\\+k {
    width: 80px;
    margin-right: 10px;
    font-size: 12px;
  }

.index__setItem__zDdi\\+ .index__value__MEW8U {
    font-size: 12px;
    width: 466px;
  }

.index__setItem__zDdi\\+ .index__valueComponent__YDwag {
    padding-top: 10px;
    width: 300px;
  }

.index__drawerSumit__iMvQH {
  margin-right: 8px !important;
}

.index__empIds__lJa-C {
  margin-bottom: 30px;
}

.index__empIds__lJa-C li {
    height: 28px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

.index__delete__QF9zh {
  color: #0070cc;
  cursor: pointer;
  margin-left: 20px;
}

.index__drawerContent__f6juk {
  padding: 20px;
}

.index__drawerContent__f6juk .index__label__slw\\+k {
    font-size: 12px;
    color: #000;
    line-height: 26px;
  }

.index__drawerContent__f6juk .index__labelTitle__CyL6d {
    font-size: 12px;
    color: #000;
    line-height: 26px;
    margin-bottom: 5px;
  }

.index__drawerContent__f6juk .index__description__HqEjg {
    color: #555;
    line-height: 20px;
  }

.index__drawerContent__f6juk .index__value__MEW8U {
    width: 100%;
    margin-bottom: 20px;
  }`,"",{version:3,sources:["webpack://./pages/Chaos/Application/ApplicationAccess/index.css"],names:[],mappings:"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",sourcesContent:[`.warp {
  .cardContent {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .pagination {
    text-align: right;
    margin-top: 16px;
  }

  .emptyData {
    display: flex;
    align-items: center;
    justify-self: start;
    margin: auto;
    margin-top: 10%;

    img {
      width: 137px;
      height: 130px;
      margin-right: 30px;
    }

    div {

      .title {
        font-family: PingFangSC-Medium;
        font-size: 18px;
        color: #222222;
        margin-bottom: 8px;
      }

      .hrefAction {
        font-family: PingFangSC-Regular;
        font-size: 12px;
        color: #0070CC;
        cursor: pointer;
      }

      div {
        line-height: 20px;
      }
    }
  }
}

.card {
  width: 384px;
  height: 136px;
  border: 1px solid #dedede;
  padding: 14px 16px 16px;
  margin-top: 16px;
  position: relative;
  cursor: pointer;
  margin-right: 16px;
  
  &:hover {
    border: 1px solid rgba(0,112,204,0.36);
    box-shadow: 0 1px 8px 0 rgba(0,112,204,0.36);
  }

  .defaultIcon {
    width: 28px;
    height: 28px;
    position: absolute;
    top: 0px;
    right: 0px;
  }

  .topContent {
    display: flex;
    justify-content: space-between;
    margin-bottom: 27px;
    line-height: 22px;

    .cardTitle {
      font-size: 14px;
      color: #333;
      width: 205px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space:nowrap;

      .tipIcon {
        margin-left: 8px;

        i {
          font-size: 16px;

          &::before {
            width: 16px;
            height: 16px;
            font-size: 16px;
            vertical-align: inherit;
          }
        }
      }
    }

    .typeTip {
      font-size: 12px;
      color: #c1c1c1;
    }
  }

  .bottomContent {
    display: flex;
    justify-content: space-between;

    .item {

      .label {
        font-size: 12px;
        color: #555;
        margin-bottom: 4px;
      }

      .value {
        font-size: 28px;
        color: #333;

      }
    }
  }

}

.actionContent {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .searchContent {
    width: 460px;
    margin-bottom: 8px;
  }
}

.appBase {
  width: 100%;
  height: 132px;
  border: 1px solid #dedede;
  padding: 16px;

  .baseTitle {
    font-size: 14px;
    color: #333;
    margin-bottom: 24px;
  }

  .content {
    display: flex;
    justify-content: space-between;

    .leftContent {
  
      .topLine {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
        line-height: 20px;
      }
  
      .bottomLine {
        display: flex;
        justify-content: flex-start;
        line-height: 20px;
      }
  
      .lineItem {
        .lineLabel {
          font-size: 12px;
          color: #888;
          display: inline-block;
          width: 108px;
        }
        
        .lineValue {
          font-size: 12px;
          color: #333;
          display: inline-block;
          width: 181px;
          margin-left: 8px;
        }
      }
  
    }
  
    .rightContent {
      display: flex;
      flex: 1;
      justify-content: space-around;
  
      .label {
        font-size: 12px;
        color: #555;
        margin-bottom: 14px;
      }

      .value {
        font-size: 28px;
        color: #333;
      }

      .groupItem {
        width: 272px;
      }
    }
  }

}

.unit {
  margin-left: 8px;
  font-size: 12px;
  color: #888;
}

.moreTag {
  font-size: 12px;
  color: #0070cc;
  cursor: pointer;
}

.icon {
  font-size: 14px;
  margin-right: 8px;

  &::before{
    font-size: 14px !important;
    width: 14px !important;
  }
}

.onLineState {
  color: #1E8E3E;
}

.notInstall {
  color: #888;
}

.offLineState {
  color: #D93026;
}

.interrupt {
  color: #FFC440;
}

.loading {
  color: #888;
}

.appAccess {
  font-family: PingFangSC-Regular;

  .title {
    font-size: 14px;
    color: #333;
  }
  
  .contentChiose {
    margin-top: 20px;
  }

  .cardContent {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .card {
    width: 180px;
    height: 72px;
    background: #F7F9FF;
    border: 1px solid #dedede;
    padding-top: 20px;
    padding-left: 25px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-right: 16px;

    .img {
      width: 28px;
      height: 28px;
      margin-right: 10px;
    }

    .name {
      font-size: 16px;
      color: #555;
    }
  }

  .chioseCard {
    background: #F3FAFF;
    border: 1px solid #0070CC;
    color: #0070CC;

    .chioseName {
      color: #0070cc;
    }
  }

  .stepContent {
    margin-top: 24px;

    :global(.next-step-item-wait .next-step-item-container .next-step-item-node-placeholder .next-step-item-node .next-step-item-node-circle) {
      background: #0070cc;
      border-color: #0070cc;
      color: #fff;
    } 

    :global(.next-step-item-wait .next-step-item-body .next-step-item-title) {
      color: #333;
    } 
  }

  .codeContent {
    width: 100%;
    padding: 16px 0 16px 24px;
    background: #F2F4F5;
    font-size: 12px;
    color: #333333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    position: relative;

    .copy {
      cursor: pointer;
      width: 48px;
      height: 36px;
      background: #dedede;
      padding: 10px 17px;
      position: absolute;
      top: 0;
      right: 0;

      .copyIcon {
        width: 14px !important;
        height: 16px !important;

        &::before{
          width: 14px !important;
          height: 16px !important;
          color: #fff;
        }
      }
    }
  }

  .codeLine {
    line-height: 20px;
  }

  .jvmParam {
    font-size: 12px;
    color: #333333;
    font-family: PingFangSC-Medium;
    margin-bottom: 6px;
  }

  .ulList {
    margin-top: 6px;
    li {
      line-height: 22px;

      &:before {
        content: "";
        width: 4px;
        height: 4px;
        display: inline-block;
        border-radius: 50%;
        background: #0070cc;
        vertical-align: middle;
        margin-right: 6px;
        }
    }
  }

  .jvmWaring {
    margin-top: 16px;
  }

  .podWord {
    font-size: 12px;
    color: #333333;
    line-height: 22px;

    .tag {
      display: inline-block;
      height: 20px;
      padding: 0 8px;
      color: #333;
      background: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 2px;
      -webkit-border-radius: 2px;
      -moz-border-radius: 2px;
      -ms-border-radius: 2px;
      -o-border-radius: 2px;
      margin: 0 2px;
    }

    .nameStyle {
      font-family: PingFangSC-Medium;
      font-size: 12px;
      color: #333333;
    }
  }

  .imageContent {
    width: 960px;
    min-height: 154px;

    img {
      width: 100%;
      height: 100%;
    }
  }

  .altWord {
    font-size: 12px;
    color: #555;
    line-height: 24px;
  }

  .guide {
    font-size: 12px;
    color: #333;
    margin-top: 24px;
    font-family: PingFangSC-Regular;
  }
}

.loading {
  padding: 15% 45%;
}

.nodeTags {
  margin-right: 4px;
  margin-bottom: 2px;
}

.setItem {
  margin-bottom: 4px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  line-height: 28px;

  .label {
    width: 80px;
    margin-right: 10px;
    font-size: 12px;
  }

  .value {
    font-size: 12px;
    width: 466px;
  }

  .valueComponent {
    padding-top: 10px;
    width: 300px;
  }
}

.drawerSumit {
  margin-right: 8px !important;
}

.empIds {
  margin-bottom: 30px;

  li {
    height: 28px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
}

.delete {
  color: #0070cc;
  cursor: pointer;
  margin-left: 20px;
}

.drawerContent {
  padding: 20px;

  .label {
    font-size: 12px;
    color: #000;
    line-height: 26px;
  }

  .labelTitle {
    font-size: 12px;
    color: #000;
    line-height: 26px;
    margin-bottom: 5px;
  }

  .description {
    color: #555;
    line-height: 20px;
  }

  .value {
    width: 100%;
    margin-bottom: 20px;
  }
}`],sourceRoot:""}]),d.locals={warp:"index__warp__TrTm0",cardContent:"index__cardContent__mLHmz",pagination:"index__pagination__xwPyo",emptyData:"index__emptyData__xV52H",title:"index__title__BqeBz",hrefAction:"index__hrefAction__pC7Qd",card:"index__card__wphgI",defaultIcon:"index__defaultIcon__nebiS",topContent:"index__topContent__XOQUb",cardTitle:"index__cardTitle__LIfnN",tipIcon:"index__tipIcon__8c1JC",typeTip:"index__typeTip__vBJEO",bottomContent:"index__bottomContent__tpr9a",item:"index__item__DdRxj",label:"index__label__slw+k",value:"index__value__MEW8U",actionContent:"index__actionContent__G0HXA",searchContent:"index__searchContent__Y4lcx",appBase:"index__appBase__uDJKb",baseTitle:"index__baseTitle__571Bf",content:"index__content__mmGfW",leftContent:"index__leftContent__hZ2Jh",topLine:"index__topLine__RZ8sR",bottomLine:"index__bottomLine__t6LZw",lineItem:"index__lineItem__a9S7Q",lineLabel:"index__lineLabel__rFDDI",lineValue:"index__lineValue__xUi3Q",rightContent:"index__rightContent__Z1N5a",groupItem:"index__groupItem__bQvEA",unit:"index__unit__n84gK",moreTag:"index__moreTag__TDApc",icon:"index__icon__m1SVF",onLineState:"index__onLineState__3TH3R",notInstall:"index__notInstall__x9rH6",offLineState:"index__offLineState__HlF9j",interrupt:"index__interrupt__zzMsc",loading:"index__loading__ieVUv",appAccess:"index__appAccess__FPVdH",contentChiose:"index__contentChiose__5dGbe",img:"index__img__gmZIV",name:"index__name__foEnk",chioseCard:"index__chioseCard__TviQG",chioseName:"index__chioseName__OY437",stepContent:"index__stepContent__gtqNI",codeContent:"index__codeContent__yzU75",copy:"index__copy__wQghF",copyIcon:"index__copyIcon__Cf84d",codeLine:"index__codeLine__a1thc",jvmParam:"index__jvmParam__FtgLJ",ulList:"index__ulList__ZhGD3",jvmWaring:"index__jvmWaring__UhCGO",podWord:"index__podWord__Rji3T",tag:"index__tag__t0aBK",nameStyle:"index__nameStyle__eeJ-1",imageContent:"index__imageContent__QeJ0A",altWord:"index__altWord__8m9mu",guide:"index__guide__teIpm",nodeTags:"index__nodeTags__TUos+",setItem:"index__setItem__zDdi+",valueComponent:"index__valueComponent__YDwag",drawerSumit:"index__drawerSumit__iMvQH",empIds:"index__empIds__lJa-C",delete:"index__delete__QF9zh",drawerContent:"index__drawerContent__f6juk",labelTitle:"index__labelTitle__CyL6d",description:"index__description__HqEjg"};const p=d},16818:(f,o,e)=>{"use strict";e.r(o),e.d(o,{default:()=>p});var l=e(1892),u=e.n(l),s=e(10975),c={};c.insert="head",c.singleton=!1;var d=u()(s.Z,c);const p=s.Z.locals||{}},78571:(f,o,e)=>{"use strict";e.r(o),e.d(o,{default:()=>p});var l=e(1892),u=e.n(l),s=e(29316),c={};c.insert="head",c.singleton=!1;var d=u()(s.Z,c);const p=s.Z.locals||{}},12561:(f,o,e)=>{"use strict";f.exports=e.p+"agentApp.733f5e1f..png"},30158:(f,o,e)=>{"use strict";f.exports=e.p+"agentHostAuto.e46e63f9..png"},67421:(f,o,e)=>{"use strict";f.exports=e.p+"agentK8s.a8329761..png"}}]);

//# sourceMappingURL=57.bundle.js.map