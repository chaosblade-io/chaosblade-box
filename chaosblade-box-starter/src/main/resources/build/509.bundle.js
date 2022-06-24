(self.webpackChunk=self.webpackChunk||[]).push([[509],{6184:function(P,f,e){var i,c,d,p=e(24596),l=e(67394),A=e(93168),M=e(23587);(function(B,x){if(!0)!(c=[f,e(77809),e(81853),e(27378),e(66697),e(14798),e(99501),e(73014),e(14870)],i=x,d=typeof i=="function"?i.apply(f,c):i,d!==void 0&&(P.exports=d));else var a})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(B,x,a,t,h,g,C,E,W){"use strict";var y=e(67971);l(B,"__esModule",{value:!0}),B.default=void 0,x=y(x),a=y(a),t=U(t),h=y(h),g=y(g),C=y(C);function u(n){if(typeof A!="function")return null;var s=new A,_=new A;return(u=function(m){return m?_:s})(n)}function U(n,s){if(!s&&n&&n.__esModule)return n;if(n===null||p(n)!=="object"&&typeof n!="function")return{default:n};var _=u(s);if(_&&_.has(n))return _.get(n);var r={},m=l&&M;for(var o in n)if(o!=="default"&&Object.prototype.hasOwnProperty.call(n,o)){var v=m?M(n,o):null;v&&(v.get||v.set)?l(r,o,v):r[o]=n[o]}return r.default=n,_&&_.set(n,r),r}var R=function(){var s=(0,W.useDispatch)(),_=(0,t.useState)(),r=(0,a.default)(_,2),m=r[0],o=r[1],v=(0,t.useState)([]),F=(0,a.default)(v,2),L=F[0],w=F[1];(0,t.useEffect)(function(){(0,x.default)(regeneratorRuntime.mark(function I(){var O,N;return regeneratorRuntime.wrap(function(S){for(;;)switch(S.prev=S.next){case 0:return S.next=2,s.workspace.getExperimentSummary();case 2:O=S.sent,N=O.Data,N&&(o(N),z(N));case 5:case"end":return S.stop()}},I)}))()},[]);function z(I){var O=[];O.push({type:g.default.t("Not as expected"),content:I.unexpectedSize}),O.push({type:g.default.t("Number of successes"),content:I.successSize}),O.push({type:g.default.t("Total"),content:I.totalSize}),w(O)}return m?t.default.createElement("div",{className:C.default.right},t.default.createElement("div",{className:C.default.header},t.default.createElement(h.default,null,"Cumulative drill data statistics")),t.default.createElement("div",null,t.default.createElement(E.Chart,{data:L,height:150,padding:[0,60,0,90],forceFit:!0},t.default.createElement(E.Coord,{transpose:!0}),t.default.createElement(E.Axis,{name:"type"}),t.default.createElement(E.Axis,{name:"content",visible:!1}),t.default.createElement(E.Geom,{type:"interval",position:"type*content"},t.default.createElement(E.Label,{content:"content",offset:5})))),t.default.createElement("div",{className:C.default.bottom},t.default.createElement("div",null,t.default.createElement(h.default,null,"Cumulative success rate"),t.default.createElement("span",{className:C.default.value},m&&Number(m.successRatio*100).toFixed(),"%")),t.default.createElement("div",null,t.default.createElement(h.default,null,"Cumulative non-compliance rate"),t.default.createElement("span",{className:C.default.value},m&&Number(m.unexpectedRatio*100).toFixed(),"%")))):null},D=R;B.default=D})},55828:function(P,f,e){var i,c,d,p=e(24596),l=e(67394),A=e(93168),M=e(23587);(function(B,x){if(!0)!(c=[f,e(81853),e(27378),e(14798),e(73014)],i=x,d=typeof i=="function"?i.apply(f,c):i,d!==void 0&&(P.exports=d));else var a})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(B,x,a,t,h){"use strict";var g=e(67971);l(B,"__esModule",{value:!0}),B.default=void 0,x=g(x),a=E(a),t=g(t);function C(u){if(typeof A!="function")return null;var U=new A,R=new A;return(C=function(n){return n?R:U})(u)}function E(u,U){if(!U&&u&&u.__esModule)return u;if(u===null||p(u)!=="object"&&typeof u!="function")return{default:u};var R=C(U);if(R&&R.has(u))return R.get(u);var D={},n=l&&M;for(var s in u)if(s!=="default"&&Object.prototype.hasOwnProperty.call(u,s)){var _=n?M(u,s):null;_&&(_.get||_.set)?l(D,s,_):D[s]=u[s]}return D.default=u,R&&R.set(u,D),D}var W=function(U){var R=(0,a.useState)([]),D=(0,x.default)(R,2),n=D[0],s=D[1];(0,a.useEffect)(function(){_(U.data)},[U.data]);function _(r){var m=[];r.forEach(function(o){m.push({type:t.default.t("Number of drills per day"),content:o.totalSize,date:o.date}),m.push({type:t.default.t("Number of successes per day"),content:o.successSize,date:o.date}),m.push({type:t.default.t("A single day does not meet expectations"),content:o.unexpectedSize,date:o.date})}),s(m)}return a.default.createElement("div",{style:{marginTop:18}},a.default.createElement(h.Chart,{height:230,data:n,forceFit:!0,padding:"auto"},a.default.createElement(h.Legend,{marker:"circle"}),a.default.createElement(h.Axis,{name:"date"}),a.default.createElement(h.Axis,{name:"content"}),a.default.createElement(h.Tooltip,{crosshairs:{type:"y"}}),a.default.createElement(h.Geom,{type:"line",position:"date*content",size:2,color:"type"}),a.default.createElement(h.Geom,{type:"point",position:"date*content",size:4,shape:"circle",color:"type",style:{stroke:"#fff",lineWidth:1}})))},y=W;B.default=y})},97079:function(P,f,e){var i,c,d,p=e(24596),l=e(67394),A=e(93168),M=e(23587);(function(B,x){if(!0)!(c=[f,e(77809),e(81853),e(27378),e(55828),e(66697),e(98784),e(95492),e(14870)],i=x,d=typeof i=="function"?i.apply(f,c):i,d!==void 0&&(P.exports=d));else var a})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(B,x,a,t,h,g,C,E,W){"use strict";var y=e(67971);l(B,"__esModule",{value:!0}),B.default=void 0,x=y(x),a=y(a),t=U(t),h=y(h),g=y(g),C=y(C),E=y(E);function u(n){if(typeof A!="function")return null;var s=new A,_=new A;return(u=function(m){return m?_:s})(n)}function U(n,s){if(!s&&n&&n.__esModule)return n;if(n===null||p(n)!=="object"&&typeof n!="function")return{default:n};var _=u(s);if(_&&_.has(n))return _.get(n);var r={},m=l&&M;for(var o in n)if(o!=="default"&&Object.prototype.hasOwnProperty.call(n,o)){var v=m?M(n,o):null;v&&(v.get||v.set)?l(r,o,v):r[o]=n[o]}return r.default=n,_&&_.set(n,r),r}var R=function(){var s=(0,W.useDispatch)(),_=(0,t.useState)([]),r=(0,a.default)(_,2),m=r[0],o=r[1],v=(0,t.useState)(0),F=(0,a.default)(v,2),L=F[0],w=F[1],z=(0,t.useState)(0),I=(0,a.default)(z,2),O=I[0],N=I[1],Q=(0,t.useState)(0),S=(0,a.default)(Q,2),b=S[0],j=S[1];return(0,t.useEffect)(function(){(0,x.default)(regeneratorRuntime.mark(function Z(){var Y,X,T;return regeneratorRuntime.wrap(function(K){for(;;)switch(K.prev=K.next){case 0:return K.next=2,s.workspace.getExperimentSummaryDays();case 2:Y=K.sent,X=Y.Data,T=X===void 0?!1:X,T&&(o(C.default.get(T,"experimentSummaryInfoList",[])),w(C.default.get(T,"successSize",0)),N(C.default.get(T,"totalSize",0)),j(C.default.get(T,"unexpectedSize",0)));case 6:case"end":return K.stop()}},Z)}))()},[]),t.default.createElement("div",{className:E.default.left},t.default.createElement("div",{className:E.default.top},t.default.createElement("div",{className:E.default.header},t.default.createElement(g.default,null,"Exercise Execution Distribution (Last 30 Days)")),t.default.createElement("div",{className:E.default.total},t.default.createElement("span",{className:E.default.itemLeft},t.default.createElement(g.default,null,"Total")),t.default.createElement("span",{className:E.default.itemRight},O),t.default.createElement("span",{className:E.default.itemLeft,style:{width:45}},t.default.createElement(g.default,null,"Number of successes")),t.default.createElement("span",{className:E.default.itemRight},L),t.default.createElement("span",{className:E.default.itemLeft,style:{width:80}},t.default.createElement(g.default,null,"Not as expected")),t.default.createElement("span",{className:E.default.itemRight},b))),C.default.isEmpty(m)?t.default.createElement("div",{className:E.default.empty},t.default.createElement("img",{src:"https://img.alicdn.com/tfs/TB1fN8awFY7gK0jSZKzXXaikpXa-268-258.png"}),t.default.createElement("div",{className:E.default.info},t.default.createElement(g.default,null,"No drills performed within 30 days"))):t.default.createElement("div",{style:{height:200}},t.default.createElement(h.default,{data:m})))},D=R;B.default=D})},68509:function(P,f,e){var i,c,d,p=e(24596),l=e(67394),A=e(93168),M=e(23587);(function(B,x){if(!0)!(c=[f,e(6184),e(27378),e(97079),e(14798),e(43751),e(96291),e(99328),e(14870)],i=x,d=typeof i=="function"?i.apply(f,c):i,d!==void 0&&(P.exports=d));else var a})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(B,x,a,t,h,g,C,E,W){"use strict";var y=e(67971);l(B,"__esModule",{value:!0}),B.default=void 0,x=y(x),a=U(a),t=y(t),h=y(h),g=y(g);function u(n){if(typeof A!="function")return null;var s=new A,_=new A;return(u=function(m){return m?_:s})(n)}function U(n,s){if(!s&&n&&n.__esModule)return n;if(n===null||p(n)!=="object"&&typeof n!="function")return{default:n};var _=u(s);if(_&&_.has(n))return _.get(n);var r={},m=l&&M;for(var o in n)if(o!=="default"&&Object.prototype.hasOwnProperty.call(n,o)){var v=m?M(n,o):null;v&&(v.get||v.set)?l(r,o,v):r[o]=n[o]}return r.default=n,_&&_.set(n,r),r}var R=function(){var s=(0,W.useDispatch)();return(0,a.useEffect)(function(){s.pageHeader.setTitle(h.default.t("Space management").toString()),s.pageHeader.setBreadCrumbItems(C.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"expertise_admin",value:h.default.t("Space management").toString(),path:"/chaos/workspace/list"}]))},[]),(0,a.useEffect)(function(){(0,E.removeParams)("workspaceId")},[]),a.default.createElement("div",{style:{marginBottom:16}},a.default.createElement("div",{className:g.default.top},a.default.createElement(t.default,null),a.default.createElement(x.default,null)))},D=R;B.default=D})},15447:(P,f,e)=>{"use strict";e.d(f,{Z:()=>A});var i=e(60994),c=e.n(i),d=e(93476),p=e.n(d),l=p()(c());l.push([P.id,`.index__right__Qo2EX {
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
`],sourceRoot:""}]),l.locals={right:"index__right__Qo2EX",header:"index__header__MPIaQ",bottom:"index__bottom__mPOdU",value:"index__value__EF47l"};const A=l},4192:(P,f,e)=>{"use strict";e.d(f,{Z:()=>A});var i=e(60994),c=e.n(i),d=e(93476),p=e.n(d),l=p()(c());l.push([P.id,`.index__left__t\\+rbh {
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
`],sourceRoot:""}]),l.locals={left:"index__left__t+rbh",top:"index__top__fwMvz",header:"index__header__tQubK",total:"index__total__88giU",itemLeft:"index__itemLeft__Rr7zU",itemRight:"index__itemRight__5TcKs",empty:"index__empty__8r2HA",info:"index__info__KOXKW"};const A=l},98472:(P,f,e)=>{"use strict";e.d(f,{Z:()=>A});var i=e(60994),c=e.n(i),d=e(93476),p=e.n(d),l=p()(c());l.push([P.id,`.index__top__HkYrd {
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
`],sourceRoot:""}]),l.locals={top:"index__top__HkYrd",header:"index__header__tukDc",workspace:"index__workspace__UbNi5",line:"index__line__XMk7X",info:"index__info__n8lEg",cardContent:"index__cardContent__EN5Xp",card:"index__card__bV2OJ",name:"index__name__c+YJe",desciption:"index__desciption__QltSL",bottomContent:"index__bottomContent__hRJ1y",item:"index__item__DdX5R",admin:"index__admin__7ONcB",left:"index__left__v7Un-",right:"index__right__Th6J+",action:"index__action__zl07t",GuidPaidDialog:"index__GuidPaidDialog__01ABP",dialogContent:"index__dialogContent__XQQNh",tips:"index__tips__4QNCS",bag:"index__bag__nv0gW"};const A=l},99501:(P,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var i=e(1892),c=e.n(i),d=e(15447),p={};p.insert="head",p.singleton=!1;var l=c()(d.Z,p);const A=d.Z.locals||{}},95492:(P,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var i=e(1892),c=e.n(i),d=e(4192),p={};p.insert="head",p.singleton=!1;var l=c()(d.Z,p);const A=d.Z.locals||{}},43751:(P,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var i=e(1892),c=e.n(i),d=e(98472),p={};p.insert="head",p.singleton=!1;var l=c()(d.Z,p);const A=d.Z.locals||{}}}]);

//# sourceMappingURL=509.bundle.js.map