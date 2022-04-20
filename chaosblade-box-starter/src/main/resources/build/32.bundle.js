(self.webpackChunk=self.webpackChunk||[]).push([[32],{11032:function(y,A,t){var r,v,f,p=t(24596),m=t(67394),c=t(93168),O=t(23587);(function(P,x){if(!0)!(v=[A,t(75453),t(36939),t(77809),t(72153),t(81853),t(50585),t(32722),t(27378),t(98784),t(28790),t(96291),t(99328),t(14870),t(42058),t(49729)],r=x,f=typeof r=="function"?r.apply(A,v):r,f!==void 0&&(y.exports=f));else var I})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(P,x,I,T,N,R,D,b,e,i,n,L,K,F,X,Z){"use strict";var d=t(67971);m(P,"__esModule",{value:!0}),P.default=void 0,x=d(x),I=d(I),T=d(T),N=d(N),R=d(R),D=d(D),b=d(b),e=Q(e),i=d(i),n=d(n);function z(a){if(typeof c!="function")return null;var u=new c,_=new c;return(z=function(B){return B?_:u})(a)}function Q(a,u){if(!u&&a&&a.__esModule)return a;if(a===null||p(a)!=="object"&&typeof a!="function")return{default:a};var _=z(u);if(_&&_.has(a))return _.get(a);var E={},B=m&&O;for(var o in a)if(o!=="default"&&Object.prototype.hasOwnProperty.call(a,o)){var C=B?O(a,o):null;C&&(C.get||C.set)?m(E,o,C):E[o]=a[o]}return E.default=a,_&&_.set(a,E),E}var $=function(){var u=(0,F.useDispatch)(),_=(0,Z.useQuery)("expertiseId"),E=(0,X.useHistory)(),B=(0,e.useState)(!1),o=(0,R.default)(B,2),C=o[0],S=o[1],G=(0,e.useState)(null),W=(0,R.default)(G,2),w=W[0],j=W[1],J=(0,F.useSelector)(function(s){return{expertiseInfo:s.expertises.expertiseInfo}}),g=J.expertiseInfo,k=i.default.get(g,"basic_info.state",0);(0,e.useEffect)(function(){u.pageHeader.setNameSpace(!1),u.pageHeader.setTitle("\u6F14\u7EC3\u7ECF\u9A8C\u5E93\u8BE6\u60C5\u9875"),u.pageHeader.setHeaderExtra(e.default.createElement("div",{style:{textAlign:"right"}},k?e.default.createElement(N.default,{type:"primary",onClick:q},"\u521B\u5EFA\u6F14\u7EC3"):null)),u.pageHeader.showBackArrow(!0),u.pageHeader.setBreadCrumbItems(L.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"expertise_list",value:"\u6F14\u7EC3\u7ECF\u9A8C\u5E93",path:"/chaos/expertise/list"},{key:"expertise_detail",value:"\u6F14\u7EC3\u7ECF\u9A8C\u5E93\u8BE6\u60C5",path:"/chaos/expertise/detail"}]))},[]),(0,e.useEffect)(function(){_&&(0,T.default)(regeneratorRuntime.mark(function s(){return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,u.expertises.getExpertiseDetail({expertise_id:_});case 2:case"end":return U.stop()}},s)}))()},[]);function q(){u.experimentEditor.setClearExperiment(),(0,K.pushUrl)(E,"/chaos/experiment/editor",{expertiseId:_})}function ee(s){j(s),S(!0)}function te(){j(null),S(!1)}var l=i.default.get(g,"basic_info",null),M=i.default.get(g,"executable_info",null),Y=i.default.get(g,"evaluation_info",{items:[]}),ne=i.default.get(M,"flow.runMode",""),H=i.default.get(M,"run_time","");return e.default.createElement("div",{className:n.default.warp},e.default.createElement("div",{className:n.default.baseInfo},e.default.createElement("div",{className:n.default.title},"\u57FA\u672C\u4FE1\u606F"),e.default.createElement("div",null,e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u7ECF\u9A8C\u540D\u79F0"),e.default.createElement("div",{className:n.default.value,title:l&&l.name},l&&l.name)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u7ECF\u9A8C\u63CF\u8FF0"),e.default.createElement("div",{className:n.default.value,title:l&&l.function_desc},l&&l.function_desc)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u6807\u7B7E"),e.default.createElement("div",{className:n.default.value},!i.default.isEmpty(l)&&l.tags.map(function(s){return e.default.createElement(I.default,{type:"primary",size:"small",key:s,className:n.default.tagCss},s)}))),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u80CC\u666F"),e.default.createElement("div",{className:n.default.value,title:l&&l.background_desc},l&&l.background_desc)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u67B6\u6784\u5F31\u70B9"),e.default.createElement("div",{className:n.default.value,title:l&&l.design_concept},l&&l.design_concept)))),e.default.createElement("div",{className:n.default.flows},e.default.createElement("div",{className:n.default.title},"\u6F14\u7EC3\u6D41\u7A0B"),e.default.createElement("div",{className:n.default.runEnvironment},e.default.createElement("div",{className:n.default.runTitle},"\u8FD0\u884C\u73AF\u5883"),e.default.createElement("div",null,!i.default.isEmpty(H)&&H.items.map(function(s){return e.default.createElement(I.default,{type:"primary",size:"small",key:s,className:n.default.tagCss},s)}))),e.default.createElement("div",null,!i.default.isEmpty(g)&&e.default.createElement(b.default,{isExpertise:!0,experiment:M,runMode:ne,onNodeClick:function(h){return ee(h)}}))),e.default.createElement("div",null,e.default.createElement("div",{className:n.default.title},"\u5176\u5B83\u4FE1\u606F"),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},"\u8BC4\u6D4B"),e.default.createElement("div",{className:n.default.value},!i.default.isEmpty(Y)&&(0,x.default)(Y.items).map(function(s,h){return e.default.createElement("li",{className:n.default.valueItem,key:h,title:s.desc},s.desc)})))),w&&e.default.createElement(D.default,{disabled:!0,readOnly:!0,isExpertise:!0,visible:C,data:w,onClose:te}))},V=$;P.default=V})},63030:(y,A,t)=>{"use strict";t.d(A,{Z:()=>c});var r=t(60994),v=t.n(r),f=t(93476),p=t.n(f),m=p()(v());m.push([y.id,`.index__warp__zTAyY .index__baseInfo__jIe1u {
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
}`],sourceRoot:""}]),m.locals={warp:"index__warp__zTAyY",baseInfo:"index__baseInfo__jIe1u",flows:"index__flows__ExfNM",title:"index__title__Hs68-",baseInfoItem:"index__baseInfoItem__TEz6H",label:"index__label__UTQof",value:"index__value__-hm27",tagCss:"index__tagCss__ecFuk",runEnvironment:"index__runEnvironment__TAvua",runTitle:"index__runTitle__j+yvR",valueItem:"index__valueItem__1pX4S"};const c=m},28790:(y,A,t)=>{"use strict";t.r(A),t.d(A,{default:()=>c});var r=t(1892),v=t.n(r),f=t(63030),p={};p.insert="head",p.singleton=!1;var m=v()(f.Z,p);const c=f.Z.locals||{}}}]);

//# sourceMappingURL=32.bundle.js.map