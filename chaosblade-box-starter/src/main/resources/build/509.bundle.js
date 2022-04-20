(self.webpackChunk=self.webpackChunk||[]).push([[509],{6184:function(v,f,e){var d,c,l,E=e(24596),r=e(67394),u=e(93168),U=e(23587);(function(g,h){if(!0)!(c=[f,e(77809),e(81853),e(27378),e(99501),e(73014),e(14870)],d=h,l=typeof d=="function"?d.apply(f,c):d,l!==void 0&&(v.exports=l));else var _})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(g,h,_,n,B,C,x){"use strict";var O=e(67971);r(g,"__esModule",{value:!0}),g.default=void 0,h=O(h),_=O(_),n=p(n),B=O(B);function P(o){if(typeof u!="function")return null;var t=new u,i=new u;return(P=function(A){return A?i:t})(o)}function p(o,t){if(!t&&o&&o.__esModule)return o;if(o===null||E(o)!=="object"&&typeof o!="function")return{default:o};var i=P(t);if(i&&i.has(o))return i.get(o);var a={},A=r&&U;for(var m in o)if(m!=="default"&&Object.prototype.hasOwnProperty.call(o,m)){var s=A?U(o,m):null;s&&(s.get||s.set)?r(a,m,s):a[m]=o[m]}return a.default=o,i&&i.set(o,a),a}var D=function(){var t=(0,x.useDispatch)(),i=(0,n.useState)(),a=(0,_.default)(i,2),A=a[0],m=a[1],s=(0,n.useState)([]),R=(0,_.default)(s,2),N=R[0],K=R[1];(0,n.useEffect)(function(){(0,h.default)(regeneratorRuntime.mark(function I(){var M,W;return regeneratorRuntime.wrap(function(S){for(;;)switch(S.prev=S.next){case 0:return S.next=2,t.workspace.getExperimentSummary();case 2:M=S.sent,W=M.Data,W&&(m(W),w(W));case 5:case"end":return S.stop()}},I)}))()},[]);function w(I){var M=[];M.push({type:"\u4E0D\u7B26\u5408\u9884\u671F\u6570",content:I.unexpectedSize}),M.push({type:"\u6210\u529F\u6570",content:I.successSize}),M.push({type:"\u603B\u6570",content:I.totalSize}),K(M)}return A?n.default.createElement("div",{className:B.default.right},n.default.createElement("div",{className:B.default.header},"\u7D2F\u8BA1\u6F14\u7EC3\u6570\u636E\u7EDF\u8BA1"),n.default.createElement("div",null,n.default.createElement(C.Chart,{data:N,height:150,padding:[0,60,0,90],forceFit:!0},n.default.createElement(C.Coord,{transpose:!0}),n.default.createElement(C.Axis,{name:"type"}),n.default.createElement(C.Axis,{name:"content",visible:!1}),n.default.createElement(C.Geom,{type:"interval",position:"type*content"},n.default.createElement(C.Label,{content:"content",offset:5})))),n.default.createElement("div",{className:B.default.bottom},n.default.createElement("div",null,"\u7D2F\u8BA1\u6210\u529F\u7387",n.default.createElement("span",{className:B.default.value},A&&Number(A.successRatio*100).toFixed(),"%")),n.default.createElement("div",null,"\u7D2F\u8BA1\u4E0D\u7B26\u5408\u9884\u671F\u7387",n.default.createElement("span",{className:B.default.value},A&&Number(A.unexpectedRatio*100).toFixed(),"%")))):null},y=D;g.default=y})},55828:function(v,f,e){var d,c,l,E=e(24596),r=e(67394),u=e(93168),U=e(23587);(function(g,h){if(!0)!(c=[f,e(81853),e(27378),e(73014)],d=h,l=typeof d=="function"?d.apply(f,c):d,l!==void 0&&(v.exports=l));else var _})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(g,h,_,n){"use strict";var B=e(67971);r(g,"__esModule",{value:!0}),g.default=void 0,h=B(h),_=x(_);function C(p){if(typeof u!="function")return null;var D=new u,y=new u;return(C=function(t){return t?y:D})(p)}function x(p,D){if(!D&&p&&p.__esModule)return p;if(p===null||E(p)!=="object"&&typeof p!="function")return{default:p};var y=C(D);if(y&&y.has(p))return y.get(p);var o={},t=r&&U;for(var i in p)if(i!=="default"&&Object.prototype.hasOwnProperty.call(p,i)){var a=t?U(p,i):null;a&&(a.get||a.set)?r(o,i,a):o[i]=p[i]}return o.default=p,y&&y.set(p,o),o}var O=function(D){var y=(0,_.useState)([]),o=(0,h.default)(y,2),t=o[0],i=o[1];(0,_.useEffect)(function(){a(D.data)},[D.data]);function a(A){var m=[];A.forEach(function(s){m.push({type:"\u5355\u65E5\u6F14\u7EC3\u6570",content:s.totalSize,date:s.date}),m.push({type:"\u5355\u65E5\u6210\u529F\u6570",content:s.successSize,date:s.date}),m.push({type:"\u5355\u65E5\u4E0D\u7B26\u5408\u9884\u671F\u6570",content:s.unexpectedSize,date:s.date})}),i(m)}return _.default.createElement("div",{style:{marginTop:18}},_.default.createElement(n.Chart,{height:230,data:t,forceFit:!0,padding:"auto"},_.default.createElement(n.Legend,{marker:"circle"}),_.default.createElement(n.Axis,{name:"date"}),_.default.createElement(n.Axis,{name:"content"}),_.default.createElement(n.Tooltip,{crosshairs:{type:"y"}}),_.default.createElement(n.Geom,{type:"line",position:"date*content",size:2,color:"type"}),_.default.createElement(n.Geom,{type:"point",position:"date*content",size:4,shape:"circle",color:"type",style:{stroke:"#fff",lineWidth:1}})))},P=O;g.default=P})},97079:function(v,f,e){var d,c,l,E=e(24596),r=e(67394),u=e(93168),U=e(23587);(function(g,h){if(!0)!(c=[f,e(77809),e(81853),e(27378),e(55828),e(98784),e(95492),e(14870)],d=h,l=typeof d=="function"?d.apply(f,c):d,l!==void 0&&(v.exports=l));else var _})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(g,h,_,n,B,C,x,O){"use strict";var P=e(67971);r(g,"__esModule",{value:!0}),g.default=void 0,h=P(h),_=P(_),n=D(n),B=P(B),C=P(C),x=P(x);function p(t){if(typeof u!="function")return null;var i=new u,a=new u;return(p=function(m){return m?a:i})(t)}function D(t,i){if(!i&&t&&t.__esModule)return t;if(t===null||E(t)!=="object"&&typeof t!="function")return{default:t};var a=p(i);if(a&&a.has(t))return a.get(t);var A={},m=r&&U;for(var s in t)if(s!=="default"&&Object.prototype.hasOwnProperty.call(t,s)){var R=m?U(t,s):null;R&&(R.get||R.set)?r(A,s,R):A[s]=t[s]}return A.default=t,a&&a.set(t,A),A}var y=function(){var i=(0,O.useDispatch)(),a=(0,n.useState)([]),A=(0,_.default)(a,2),m=A[0],s=A[1],R=(0,n.useState)(0),N=(0,_.default)(R,2),K=N[0],w=N[1],I=(0,n.useState)(0),M=(0,_.default)(I,2),W=M[0],z=M[1],S=(0,n.useState)(0),X=(0,_.default)(S,2),Y=X[0],b=X[1];return(0,n.useEffect)(function(){(0,h.default)(regeneratorRuntime.mark(function j(){var Q,L,F;return regeneratorRuntime.wrap(function(T){for(;;)switch(T.prev=T.next){case 0:return T.next=2,i.workspace.getExperimentSummaryDays();case 2:Q=T.sent,L=Q.Data,F=L===void 0?!1:L,F&&(s(C.default.get(F,"experimentSummaryInfoList",[])),w(C.default.get(F,"successSize",0)),z(C.default.get(F,"totalSize",0)),b(C.default.get(F,"unexpectedSize",0)));case 6:case"end":return T.stop()}},j)}))()},[]),n.default.createElement("div",{className:x.default.left},n.default.createElement("div",{className:x.default.top},n.default.createElement("div",{className:x.default.header},"\u6F14\u7EC3\u6267\u884C\u5206\u5E03\uFF08\u8FD130\u5929\uFF09"),n.default.createElement("div",{className:x.default.total},n.default.createElement("span",{className:x.default.itemLeft},"\u603B\u6570"),n.default.createElement("span",{className:x.default.itemRight},W),n.default.createElement("span",{className:x.default.itemLeft,style:{width:45}},"\u6210\u529F\u6570"),n.default.createElement("span",{className:x.default.itemRight},K),n.default.createElement("span",{className:x.default.itemLeft,style:{width:80}},"\u4E0D\u7B26\u5408\u9884\u671F\u6570"),n.default.createElement("span",{className:x.default.itemRight},Y))),C.default.isEmpty(m)?n.default.createElement("div",{className:x.default.empty},n.default.createElement("img",{src:"https://img.alicdn.com/tfs/TB1fN8awFY7gK0jSZKzXXaikpXa-268-258.png"}),n.default.createElement("div",{className:x.default.info},"30\u5929\u5185\u672A\u6267\u884C\u8FC7\u6F14\u7EC3")):n.default.createElement("div",{style:{height:200}},n.default.createElement(B.default,{data:m})))},o=y;g.default=o})},68509:function(v,f,e){var d,c,l,E=e(24596),r=e(67394),u=e(93168),U=e(23587);(function(g,h){if(!0)!(c=[f,e(6184),e(27378),e(97079),e(43751),e(96291),e(99328),e(14870)],d=h,l=typeof d=="function"?d.apply(f,c):d,l!==void 0&&(v.exports=l));else var _})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(g,h,_,n,B,C,x,O){"use strict";var P=e(67971);r(g,"__esModule",{value:!0}),g.default=void 0,h=P(h),_=D(_),n=P(n),B=P(B);function p(t){if(typeof u!="function")return null;var i=new u,a=new u;return(p=function(m){return m?a:i})(t)}function D(t,i){if(!i&&t&&t.__esModule)return t;if(t===null||E(t)!=="object"&&typeof t!="function")return{default:t};var a=p(i);if(a&&a.has(t))return a.get(t);var A={},m=r&&U;for(var s in t)if(s!=="default"&&Object.prototype.hasOwnProperty.call(t,s)){var R=m?U(t,s):null;R&&(R.get||R.set)?r(A,s,R):A[s]=t[s]}return A.default=t,a&&a.set(t,A),A}var y=function(){var i=(0,O.useDispatch)();return(0,_.useEffect)(function(){i.pageHeader.setTitle("\u7A7A\u95F4\u7BA1\u7406"),i.pageHeader.setBreadCrumbItems(C.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"expertise_admin",value:"\u7A7A\u95F4\u7BA1\u7406",path:"/chaos/workspace/list"}]))},[]),(0,_.useEffect)(function(){(0,x.removeParams)("workspaceId")},[]),_.default.createElement("div",{style:{marginBottom:16}},_.default.createElement("div",{className:B.default.top},_.default.createElement(n.default,null),_.default.createElement(h.default,null)))},o=y;g.default=o})},15447:(v,f,e)=>{"use strict";e.d(f,{Z:()=>u});var d=e(60994),c=e.n(d),l=e(93476),E=e.n(l),r=E()(c());r.push([v.id,`.index__right__Qo2EX {
  width: 39.5%;
  height: 288px;
  padding: 16px;
  border: 1px solid #dedede;

}

  .index__right__Qo2EX .index__header__MPIaQ {
    color: #333333;
    font-size: 14px;
    font-weight: 700;
    margin-bottom: 18px;
  }

  .index__right__Qo2EX .index__bottom__mPOdU {
    margin-top: 18px;
    color: #888888;
    font-size: 12px;
    line-height: 24px;
  }

  .index__right__Qo2EX .index__bottom__mPOdU .index__value__EF47l {
      color: #333333;
      margin-left: 28px;
    }
`,"",{version:3,sources:["webpack://./pages/Chaos/WorkSpace/DrillTotalStatistics/index.css"],names:[],mappings:"AAAA;EACE,YAAY;EACZ,aAAa;EACb,aAAa;EACb,yBAAyB;;AAoB3B;;EAlBE;IACE,cAAc;IACd,eAAe;IACf,gBAAgB;IAChB,mBAAmB;EACrB;;EAEA;IACE,gBAAgB;IAChB,cAAc;IACd,eAAe;IACf,iBAAiB;EAKnB;;EAJE;MACE,cAAc;MACd,iBAAiB;IACnB",sourcesContent:[`.right {
  width: 39.5%;
  height: 288px;
  padding: 16px;
  border: 1px solid #dedede;

  .header {
    color: #333333;
    font-size: 14px;
    font-weight: 700;
    margin-bottom: 18px;
  }

  .bottom {
    margin-top: 18px;
    color: #888888;
    font-size: 12px;
    line-height: 24px;
    .value {
      color: #333333;
      margin-left: 28px;
    }
  }

}
`],sourceRoot:""}]),r.locals={right:"index__right__Qo2EX",header:"index__header__MPIaQ",bottom:"index__bottom__mPOdU",value:"index__value__EF47l"};const u=r},4192:(v,f,e)=>{"use strict";e.d(f,{Z:()=>u});var d=e(60994),c=e.n(d),l=e(93476),E=e.n(l),r=E()(c());r.push([v.id,`.index__left__t\\+rbh {
  width: 60%;
  height: 288px;
  padding: 16px;
  border: 1px solid #dedede;
  margin-right: 16px;
}

  .index__left__t\\+rbh .index__top__fwMvz {
    display: flex;
    justify-content: space-between;
  }

  .index__left__t\\+rbh .index__header__tQubK {
    color: #333333;
    font-size: 14px;
    font-weight: 700;
  }

  .index__left__t\\+rbh .index__total__88giU {
    color: #888888;
    font-size: 12px;
  }

  .index__left__t\\+rbh .index__total__88giU .index__itemLeft__Rr7zU {
      display: inline-block;
      width: 36px;
    }

  .index__left__t\\+rbh .index__total__88giU .index__itemRight__5TcKs {
      display: inline-block;
      margin-right: 8px;
      color: #333333;
    }

.index__empty__8r2HA {
  margin-top: 60px;
  text-align: center;
}

.index__empty__8r2HA img {
    width: 100px;
  }

.index__empty__8r2HA .index__info__KOXKW {
    font-size: 14px;
    font-weight: 600;
  }
`,"",{version:3,sources:["webpack://./pages/Chaos/WorkSpace/TaskInfoDistribute/index.css"],names:[],mappings:"AAAA;EACE,UAAU;EACV,aAAa;EACb,aAAa;EACb,yBAAyB;EACzB,kBAAkB;AA0BpB;;EAxBE;IACE,aAAa;IACb,8BAA8B;EAChC;;EAEA;IACE,cAAc;IACd,eAAe;IACf,gBAAgB;EAClB;;EAEA;IACE,cAAc;IACd,eAAe;EAUjB;;EATE;MACE,qBAAqB;MACrB,WAAW;IACb;;EACA;MACE,qBAAqB;MACrB,iBAAiB;MACjB,cAAc;IAChB;;AAIJ;EACE,gBAAgB;EAChB,kBAAkB;AAQpB;;AAPE;IACE,YAAY;EACd;;AACA;IACE,eAAe;IACf,gBAAgB;EAClB",sourcesContent:[`.left {
  width: 60%;
  height: 288px;
  padding: 16px;
  border: 1px solid #dedede;
  margin-right: 16px;

  .top {
    display: flex;
    justify-content: space-between;
  }

  .header {
    color: #333333;
    font-size: 14px;
    font-weight: 700;
  }

  .total {
    color: #888888;
    font-size: 12px;
    .itemLeft {
      display: inline-block;
      width: 36px;
    }
    .itemRight {
      display: inline-block;
      margin-right: 8px;
      color: #333333;
    }
  }
}

.empty {
  margin-top: 60px;
  text-align: center;
  img {
    width: 100px;
  }
  .info {
    font-size: 14px;
    font-weight: 600;
  }
}
`],sourceRoot:""}]),r.locals={left:"index__left__t+rbh",top:"index__top__fwMvz",header:"index__header__tQubK",total:"index__total__88giU",itemLeft:"index__itemLeft__Rr7zU",itemRight:"index__itemRight__5TcKs",empty:"index__empty__8r2HA",info:"index__info__KOXKW"};const u=r},98472:(v,f,e)=>{"use strict";e.d(f,{Z:()=>u});var d=e(60994),c=e.n(d),l=e(93476),E=e.n(l),r=E()(c());r.push([v.id,`.index__top__HkYrd {
  display: flex;
}

.index__header__tukDc {
  color: #333333;
  font-size: 14px;
  font-weight: 700;
}

.index__workspace__UbNi5 {
  margin-top: 24px;
}

.index__workspace__UbNi5 .index__line__XMk7X {
    width: 100%;
    height: 1px;
    background: #dedede;
    margin-top: 24px;
    margin-bottom: 8px;
  }

.index__workspace__UbNi5 .index__info__n8lEg {
    margin-top: 16px;
    display: flex;
    margin-bottom: 8px;
  }

.index__workspace__UbNi5 .index__cardContent__EN5Xp {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
  }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ {
      width: 384px;
      border: 1px solid #dedede;
      padding: 14px 16px 16px;
      cursor: pointer;
      height: 176px;
      margin-right: 16px;
      margin-top: 16px;
    }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ:hover {
        border: 1px solid rgba(0, 112, 204, 0.36);
        box-shadow: 0 1px 8px 0 rgba(0, 112, 204, 0.36);
      }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ .index__name__c\\+YJe {
        color: #333333;
        font-size: 14px;
        font-weight: 700;
        display: flex;
        justify-content: space-between;
      }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ .index__desciption__QltSL {
        color: #555555;
        font-size: 12px;
        margin-bottom: 24px;
        margin-top: 15px;
        height: 52px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ .index__bottomContent__hRJ1y {
        display: flex;
      }

.index__workspace__UbNi5 .index__cardContent__EN5Xp .index__card__bV2OJ .index__bottomContent__hRJ1y .index__item__DdX5R {
          margin-right: 82px;
        }

.index__admin__7ONcB {
  display: flex;
  margin-bottom: 35px;
}

.index__admin__7ONcB .index__left__v7Un- {
    width: 48px;
    color: #333333;
  }

.index__admin__7ONcB .index__right__Th6J\\+ {
    width: 200px;
    height: 20px;
    text-overflow: ellipsis;
    overflow: hidden;
  }

.index__action__zl07t {
  color: #0070cc;
  cursor: pointer;
  font-size: 12px;
}

/* \u8D44\u6E90\u5305\u5F39\u7A97 */
.index__GuidPaidDialog__01ABP {
  width: 800px;
  height: 224px;
  padding: 40px;
}
.index__GuidPaidDialog__01ABP .next-dialog-body {
    padding: 0px !important;
  }
.index__GuidPaidDialog__01ABP .index__dialogContent__XQQNh {
    width: 100%;
    display: flex;
    justify-content: flex-start;
  }

.index__tips__4QNCS {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #333333;
  text-align: left;
  line-height: 20px; 
}

.index__bag__nv0gW {
  height: 20px;
  width: 44px;
  background: #0091FF;
  border-radius: 2px;
  background-color: #0091FF;
  padding: 0 2px;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #FFFFFF;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/WorkSpace/index.css"],names:[],mappings:"AAAA;EACE,aAAa;AACf;;AAEA;EACE,cAAc;EACd,eAAe;EACf,gBAAgB;AAClB;;AAEA;EACE,gBAAgB;AA0DlB;;AAxDE;IACE,WAAW;IACX,WAAW;IACX,mBAAmB;IACnB,gBAAgB;IAChB,kBAAkB;EACpB;;AAEA;IACE,gBAAgB;IAChB,aAAa;IACb,kBAAkB;EACpB;;AACA;IACE,aAAa;IACb,2BAA2B;IAC3B,eAAe;EAuCjB;;AAtCE;MACE,YAAY;MACZ,yBAAyB;MACzB,uBAAuB;MACvB,eAAe;MACf,aAAa;MACb,kBAAkB;MAClB,gBAAgB;IA8BlB;;AA7BE;QACE,yCAAyC;QACzC,+CAA+C;MACjD;;AACA;QACE,cAAc;QACd,eAAe;QACf,gBAAgB;QAChB,aAAa;QACb,8BAA8B;MAChC;;AACA;QACE,cAAc;QACd,eAAe;QACf,mBAAmB;QACnB,gBAAgB;QAChB,YAAY;QACZ,gBAAgB;QAChB,uBAAuB;QACvB,oBAAoB;QACpB,qBAAqB;QACrB,4BAA4B;MAC9B;;AACA;QACE,aAAa;MAIf;;AAHE;UACE,kBAAkB;QACpB;;AAMR;EACE,aAAa;EACb,mBAAmB;AAWrB;;AAVE;IACE,WAAW;IACX,cAAc;EAChB;;AACA;IACE,YAAY;IACZ,YAAY;IACZ,uBAAuB;IACvB,gBAAgB;EAClB;;AAGF;EACE,cAAc;EACd,eAAe;EACf,eAAe;AACjB;;AAEA,UAAU;AACV;EACE,YAAY;EACZ,aAAa;EACb,aAAa;AAWf;AATE;IACE,uBAAuB;EACzB;AAEA;IACE,WAAW;IACX,aAAa;IACb,2BAA2B;EAC7B;;AAGF;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,gBAAgB;EAChB,iBAAiB;AACnB;;AAEA;EACE,YAAY;EACZ,WAAW;EACX,mBAAmB;EACnB,kBAAkB;EAClB,yBAAyB;EACzB,cAAc;EACd,+BAA+B;EAC/B,eAAe;EACf,cAAc;AAChB",sourcesContent:[`.top {
  display: flex;
}

.header {
  color: #333333;
  font-size: 14px;
  font-weight: 700;
}

.workspace {
  margin-top: 24px;

  .line {
    width: 100%;
    height: 1px;
    background: #dedede;
    margin-top: 24px;
    margin-bottom: 8px;
  }

  .info {
    margin-top: 16px;
    display: flex;
    margin-bottom: 8px;
  }
  .cardContent {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    .card {
      width: 384px;
      border: 1px solid #dedede;
      padding: 14px 16px 16px;
      cursor: pointer;
      height: 176px;
      margin-right: 16px;
      margin-top: 16px;
      &:hover {
        border: 1px solid rgba(0, 112, 204, 0.36);
        box-shadow: 0 1px 8px 0 rgba(0, 112, 204, 0.36);
      }
      .name {
        color: #333333;
        font-size: 14px;
        font-weight: 700;
        display: flex;
        justify-content: space-between;
      }
      .desciption {
        color: #555555;
        font-size: 12px;
        margin-bottom: 24px;
        margin-top: 15px;
        height: 52px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
      .bottomContent {
        display: flex;
        .item {
          margin-right: 82px;
        }
      }
    }
  }
}

.admin {
  display: flex;
  margin-bottom: 35px;
  .left {
    width: 48px;
    color: #333333;
  }
  .right {
    width: 200px;
    height: 20px;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}

.action {
  color: #0070cc;
  cursor: pointer;
  font-size: 12px;
}

/* \u8D44\u6E90\u5305\u5F39\u7A97 */
.GuidPaidDialog {
  width: 800px;
  height: 224px;
  padding: 40px;

  :global(.next-dialog-body) {
    padding: 0px !important;
  }

  .dialogContent {
    width: 100%;
    display: flex;
    justify-content: flex-start;
  }
}

.tips {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #333333;
  text-align: left;
  line-height: 20px; 
}

.bag {
  height: 20px;
  width: 44px;
  background: #0091FF;
  border-radius: 2px;
  background-color: #0091FF;
  padding: 0 2px;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #FFFFFF;
}
`],sourceRoot:""}]),r.locals={top:"index__top__HkYrd",header:"index__header__tukDc",workspace:"index__workspace__UbNi5",line:"index__line__XMk7X",info:"index__info__n8lEg",cardContent:"index__cardContent__EN5Xp",card:"index__card__bV2OJ",name:"index__name__c+YJe",desciption:"index__desciption__QltSL",bottomContent:"index__bottomContent__hRJ1y",item:"index__item__DdX5R",admin:"index__admin__7ONcB",left:"index__left__v7Un-",right:"index__right__Th6J+",action:"index__action__zl07t",GuidPaidDialog:"index__GuidPaidDialog__01ABP",dialogContent:"index__dialogContent__XQQNh",tips:"index__tips__4QNCS",bag:"index__bag__nv0gW"};const u=r},99501:(v,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>u});var d=e(1892),c=e.n(d),l=e(15447),E={};E.insert="head",E.singleton=!1;var r=c()(l.Z,E);const u=l.Z.locals||{}},95492:(v,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>u});var d=e(1892),c=e.n(d),l=e(4192),E={};E.insert="head",E.singleton=!1;var r=c()(l.Z,E);const u=l.Z.locals||{}},43751:(v,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>u});var d=e(1892),c=e.n(d),l=e(98472),E={};E.insert="head",E.singleton=!1;var r=c()(l.Z,E);const u=l.Z.locals||{}}}]);

//# sourceMappingURL=509.bundle.js.map