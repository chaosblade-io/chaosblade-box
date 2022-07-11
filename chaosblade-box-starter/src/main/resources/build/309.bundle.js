(self.webpackChunk=self.webpackChunk||[]).push([[309],{11032:function(y,c,t){var i,p,f,A=t(24596),u=t(67394),r=t(93168),C=t(23587);(function(v,h){if(!0)!(p=[c,t(75453),t(36939),t(77809),t(72153),t(81853),t(50585),t(32722),t(27378),t(66697),t(98784),t(14798),t(28790),t(96291),t(99328),t(14870),t(42058),t(49729)],i=h,f=typeof i=="function"?i.apply(c,p):i,f!==void 0&&(y.exports=f));else var g})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(v,h,g,b,R,B,P,U,e,n,d,M,l,$,X,z,Z,Q){"use strict";var m=t(67971);u(v,"__esModule",{value:!0}),v.default=void 0,h=m(h),g=m(g),b=m(b),R=m(R),B=m(B),P=m(P),U=m(U),e=G(e),n=m(n),d=m(d),M=m(M),l=m(l);function W(a){if(typeof r!="function")return null;var _=new r,E=new r;return(W=function(N){return N?E:_})(a)}function G(a,_){if(!_&&a&&a.__esModule)return a;if(a===null||A(a)!=="object"&&typeof a!="function")return{default:a};var E=W(_);if(E&&E.has(a))return E.get(a);var I={},N=u&&C;for(var x in a)if(x!=="default"&&Object.prototype.hasOwnProperty.call(a,x)){var D=N?C(a,x):null;D&&(D.get||D.set)?u(I,x,D):I[x]=a[x]}return I.default=a,E&&E.set(a,I),I}var V=function(){var _=(0,z.useDispatch)(),E=(0,Q.useQuery)("expertiseId"),I=(0,Z.useHistory)(),N=(0,e.useState)(!1),x=(0,B.default)(N,2),D=x[0],F=x[1],k=(0,e.useState)(null),j=(0,B.default)(k,2),Y=j[0],w=j[1],q=(0,z.useSelector)(function(o){return{expertiseInfo:o.expertises.expertiseInfo}}),T=q.expertiseInfo,ee=d.default.get(T,"basic_info.state",0);(0,e.useEffect)(function(){_.pageHeader.setNameSpace(!1),_.pageHeader.setTitle(e.default.createElement(n.default,null,"Drill experience details page")),_.pageHeader.setHeaderExtra(e.default.createElement("div",{style:{textAlign:"right"}},ee?e.default.createElement(R.default,{type:"primary",onClick:te},e.default.createElement(n.default,null,"create drill")):null)),_.pageHeader.showBackArrow(!0),_.pageHeader.setBreadCrumbItems($.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"expertise_list",value:M.default.t("drill experience library"),path:"/chaos/expertise/list"},{key:"expertise_detail",value:M.default.t("Drill experience details page"),path:"/chaos/expertise/detail"}]))},[]),(0,e.useEffect)(function(){E&&(0,b.default)(regeneratorRuntime.mark(function o(){return regeneratorRuntime.wrap(function(O){for(;;)switch(O.prev=O.next){case 0:return O.next=2,_.expertises.getExpertiseDetail({expertise_id:E});case 2:case"end":return O.stop()}},o)}))()},[]);function te(){_.experimentEditor.setClearExperiment(),(0,X.pushUrl)(I,"/chaos/experiment/editor",{expertiseId:E})}function le(o){w(o),F(!0)}function ne(){w(null),F(!1)}var s=d.default.get(T,"basic_info",null),L=d.default.get(T,"executable_info",null),K=d.default.get(T,"evaluation_info",{items:[]}),ae=d.default.get(L,"flow.runMode",""),H=d.default.get(L,"run_time","");return e.default.createElement("div",{className:l.default.warp},e.default.createElement("div",{className:l.default.baseInfo},e.default.createElement("div",{className:l.default.title},e.default.createElement(n.default,null,"Basic Information")),e.default.createElement("div",null,e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"Experience name")),e.default.createElement("div",{className:l.default.value,title:s&&s.name},s&&s.name)),e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"Experience description")),e.default.createElement("div",{className:l.default.value,title:s&&s.function_desc},s&&s.function_desc)),e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"Tag")),e.default.createElement("div",{className:l.default.value},!d.default.isEmpty(s)&&s.tags.map(function(o){return e.default.createElement(g.default,{type:"primary",size:"small",key:o,className:l.default.tagCss},o)}))),e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"background")),e.default.createElement("div",{className:l.default.value,title:s&&s.background_desc},s&&s.background_desc)),e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"Architectural Weaknesses")),e.default.createElement("div",{className:l.default.value,title:s&&s.design_concept},s&&s.design_concept)))),e.default.createElement("div",{className:l.default.flows},e.default.createElement("div",{className:l.default.title},e.default.createElement(n.default,null,"Exercise process")),e.default.createElement("div",{className:l.default.runEnvironment},e.default.createElement("div",{className:l.default.runTitle},e.default.createElement(n.default,null,"Operating environment")),e.default.createElement("div",null,!d.default.isEmpty(H)&&H.items.map(function(o){return e.default.createElement(g.default,{type:"primary",size:"small",key:o,className:l.default.tagCss},o)}))),e.default.createElement("div",null,!d.default.isEmpty(T)&&e.default.createElement(U.default,{isExpertise:!0,experiment:L,runMode:ae,onNodeClick:function(S){return le(S)}}))),e.default.createElement("div",null,e.default.createElement("div",{className:l.default.title},e.default.createElement(n.default,null,"Other information")),e.default.createElement("div",{className:l.default.baseInfoItem},e.default.createElement("div",{className:l.default.label},e.default.createElement(n.default,null,"evaluating")),e.default.createElement("div",{className:l.default.value},!d.default.isEmpty(K)&&(0,h.default)(K.items).map(function(o,S){return e.default.createElement("li",{className:l.default.valueItem,key:S,title:o.desc},o.desc)})))),Y&&e.default.createElement(P.default,{disabled:!0,readOnly:!0,isExpertise:!0,visible:D,data:Y,onClose:ne}))},J=V;v.default=J})},68055:function(y,c,t){var i,p,f,A=t(67394);(function(u,r){if(!0)!(p=[c,t(46454),t(17014),t(57379),t(13026)],i=r,f=typeof i=="function"?i.apply(c,p):i,f!==void 0&&(y.exports=f));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(u,r,C,v,h){"use strict";var g=t(67971);A(u,"__esModule",{value:!0}),u.default=void 0,r=g(r),C=g(C),v=g(v);var b={en:{Timeline:{expand:"Expand",fold:"Fold"},Balloon:{close:"Close"},Card:{expand:"Expand",fold:"Fold"},Dialog:{close:"Close",ok:"Confirm",cancel:"Cancel"},Drawer:{close:"Close"},Message:{closeAriaLabel:"Close"},Pagination:{prev:"Prev",next:"Next",goTo:"Go To",page:"Page",go:"Go",total:"Page {current} of {total} pages.",labelPrev:"Prev page, current page {current}",labelNext:"Next page, current page {current}",inputAriaLabel:"Please enter the page to jump to",selectAriaLabel:"Please select page size",pageSize:"Page Size:"},Input:{clear:"Clear"},List:{empty:"No Data"},Select:{selectPlaceholder:"Please select",autoCompletePlaceholder:"Please enter",notFoundContent:"No Options",maxTagPlaceholder:"{selected}/{total} items have been selected.",selectAll:"Select All"},Table:{empty:"No Data",ok:"Confirm",reset:"Reset",asc:"Asc",desc:"Desc",expanded:"Expanded",folded:"Folded",filter:"Filter",selectAll:"Select All"},Upload:{card:{cancel:"Cancel",delete:"Delete"},upload:{delete:"Delete"}},Search:{buttonText:"Search"},Tag:{delete:"Delete"},Switch:{on:"On",off:"Off"},Tab:{closeAriaLabel:"Close"},Dropdown:{empty:"No Data"},Radio:{empty:"No Data"}},zh:{Timeline:{expand:"\u5C55\u5F00",fold:"\u6536\u8D77"},Balloon:{close:"\u5173\u95ED"},Card:{expand:"\u5C55\u5F00",fold:"\u6536\u8D77"},Dialog:{close:"\u5173\u95ED",ok:"\u786E\u8BA4",cancel:"\u53D6\u6D88"},Drawer:{close:"\u5173\u95ED"},Message:{closeAriaLabel:"\u5173\u95ED\u6807\u7B7E"},Pagination:{prev:"\u524D\u4E00\u9875",next:"\u4E0B\u4E00\u9875",goTo:"\u53BB\u5F80",page:"\u5206\u9875",go:"\u53BB",total:"Page {current} of {total} pages.",labelPrev:"\u524D\u4E00\u9875, \u5F53\u524D\u9875 {current}",labelNext:"\u4E0B\u4E00\u9875, \u5F53\u524D\u9875 {current}",inputAriaLabel:"\u8BF7\u8F93\u5165\u8981\u8DF3\u8F6C\u5230\u7684\u9875\u9762",selectAriaLabel:"\u8BF7\u9009\u62E9\u9875\u9762\u5C55\u793A\u7684\u6570\u91CF",pageSize:"\u6BCF\u9875\u663E\u793A\u591A\u5C11\u6761:"},Input:{clear:"\u6E05\u7A7A"},List:{empty:"\u6CA1\u6709\u6570\u636E"},Select:{selectPlaceholder:"\u8BF7\u9009\u62E9",autoCompletePlaceholder:"\u8BF7\u8F93\u5165",notFoundContent:"\u6CA1\u6709\u4E0B\u62C9\u9879",maxTagPlaceholder:"{selected}/{total} \u6761\u76EE\u5DF2\u9009\u62E9.",selectAll:"\u5168\u9009"},Table:{empty:"\u6CA1\u6709\u6570\u636E",ok:"\u786E\u8BA4",reset:"\u91CD\u7F6E",asc:"\u751F\u5E8F",desc:"\u964D\u5E8F",expanded:"\u5C55\u5F00",folded:"\u6536\u8D77",filter:"\u8FC7\u6EE4",selectAll:"\u5168\u9009"},Upload:{card:{cancel:"\u53D6\u6D88",delete:"\u5220\u9664"},upload:{delete:"\u5220\u9664"}},Search:{buttonText:"\u641C\u7D22"},Tag:{delete:"\u5220\u9664"},Switch:{on:"\u6253\u5F00",off:"\u5173\u95ED"},Tab:{closeAriaLabel:"\u5173\u95ED"},Dropdown:{empty:"\u6CA1\u6709\u6570\u636E"},Radio:{empty:"\u6CA1\u6709\u6570\u636E"}}},R=function(){function P(){(0,r.default)(this,P),(0,v.default)(this,"local",void 0)}return(0,C.default)(P,[{key:"setLocal",value:function(e){this.local=e}},{key:"getLocal",value:function(){var e=this;return function(){var n,d=(0,h.getLanguage)();return(n=e.local[d])!==null&&n!==void 0?n:e.local.en}}}],[{key:"getInstance",value:function(){return this.instance=this.instance||new P,this.instance}}]),P}();(0,v.default)(R,"instance",null),R.getInstance().setLocal(b);var B=R.getInstance().getLocal();u.default=B})},63030:(y,c,t)=>{"use strict";t.d(c,{Z:()=>r});var i=t(60994),p=t.n(i),f=t(93476),A=t.n(f),u=A()(p());u.push([y.id,`.index__warp__zTAyY .index__baseInfo__jIe1u {
    padding-bottom: 16px;
    border-bottom: 1px solid #dedede;
    margin-bottom: 24px;
  }

  .index__warp__zTAyY .index__flows__ExfNM {
    padding-bottom: 24px;
    margin-bottom: 24px;
    border-bottom: 1px solid #dedede;
  }

  .index__warp__zTAyY .index__title__Hs68- {
    font-size: 14px;
    color: #333333;
    line-height: 32px;
    margin-bottom: 8px;
  }

  .index__warp__zTAyY .index__baseInfoItem__TEz6H {
    font-size: 12px;
    color: #555555;
    line-height: 20px;
    margin-bottom: 12px;
    display: flex;
    justify-content: flex-start;
  }

  .index__warp__zTAyY .index__baseInfoItem__TEz6H .index__label__UTQof {
      min-width: 100px;
    }

  .index__warp__zTAyY .index__baseInfoItem__TEz6H .index__value__-hm27 {
      width: 80%;
    }

  .index__warp__zTAyY .index__tagCss__ecFuk {
    margin-right: 5px;
  }

  .index__warp__zTAyY .index__runEnvironment__TAvua {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 16px;
  }

  .index__warp__zTAyY .index__runEnvironment__TAvua .index__runTitle__j\\+yvR {
      width: 100px;
      font-size: 12px;
      color: #555;
    }

  .index__warp__zTAyY .index__valueItem__1pX4S {
    line-height: 18px;
    width: 90%;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin-bottom: 6px;
    list-style: circle;
  }`,"",{version:3,sources:["webpack://./pages/Chaos/ExpertiseList/ExpertiseDetail/index.css"],names:[],mappings:"AAEE;IACE,oBAAoB;IACpB,gCAAgC;IAChC,mBAAmB;EACrB;;EAEA;IACE,oBAAoB;IACpB,mBAAmB;IACnB,gCAAgC;EAClC;;EAEA;IACE,eAAe;IACf,cAAc;IACd,iBAAiB;IACjB,kBAAkB;EACpB;;EAEA;IACE,eAAe;IACf,cAAc;IACd,iBAAiB;IACjB,mBAAmB;IACnB,aAAa;IACb,2BAA2B;EAS7B;;EAPE;MACE,gBAAgB;IAClB;;EAEA;MACE,UAAU;IACZ;;EAGF;IACE,iBAAiB;EACnB;;EAEA;IACE,aAAa;IACb,2BAA2B;IAC3B,mBAAmB;EAOrB;;EALE;MACE,YAAY;MACZ,eAAe;MACf,WAAW;IACb;;EAGF;IACE,iBAAiB;IACjB,UAAU;IACV,mBAAmB;IACnB,uBAAuB;IACvB,kBAAkB;IAClB,kBAAkB;EACpB",sourcesContent:[`.warp {

  .baseInfo {
    padding-bottom: 16px;
    border-bottom: 1px solid #dedede;
    margin-bottom: 24px;
  }
  
  .flows {
    padding-bottom: 24px;
    margin-bottom: 24px;
    border-bottom: 1px solid #dedede;
  }

  .title {
    font-size: 14px;
    color: #333333;
    line-height: 32px;
    margin-bottom: 8px;
  }

  .baseInfoItem {
    font-size: 12px;
    color: #555555;
    line-height: 20px;
    margin-bottom: 12px;
    display: flex;
    justify-content: flex-start;

    .label {
      min-width: 100px;
    }

    .value {
      width: 80%;
    }
  }

  .tagCss {
    margin-right: 5px;
  }

  .runEnvironment {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 16px;

    .runTitle {
      width: 100px;
      font-size: 12px;
      color: #555;
    }
  }

  .valueItem {
    line-height: 18px;
    width: 90%;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin-bottom: 6px;
    list-style: circle;
  }
}`],sourceRoot:""}]),u.locals={warp:"index__warp__zTAyY",baseInfo:"index__baseInfo__jIe1u",flows:"index__flows__ExfNM",title:"index__title__Hs68-",baseInfoItem:"index__baseInfoItem__TEz6H",label:"index__label__UTQof",value:"index__value__-hm27",tagCss:"index__tagCss__ecFuk",runEnvironment:"index__runEnvironment__TAvua",runTitle:"index__runTitle__j+yvR",valueItem:"index__valueItem__1pX4S"};const r=u},28790:(y,c,t)=>{"use strict";t.r(c),t.d(c,{default:()=>r});var i=t(1892),p=t.n(i),f=t(63030),A={};A.insert="head",A.singleton=!1;var u=p()(f.Z,A);const r=f.Z.locals||{}}}]);

//# sourceMappingURL=309.bundle.js.map