(self.webpackChunk=self.webpackChunk||[]).push([[32],{11032:function(B,A,t){var r,x,m,c=t(24596),E=t(67394),v=t(93168),S=t(23587);(function(U,C){if(!0)!(x=[A,t(75453),t(36939),t(77809),t(72153),t(81853),t(50585),t(32722),t(27378),t(66697),t(98784),t(14798),t(28790),t(96291),t(99328),t(14870),t(42058),t(49729)],r=C,m=typeof r=="function"?r.apply(A,x):r,m!==void 0&&(B.exports=m));else var P})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(U,C,P,D,M,y,T,O,e,d,i,h,n,X,Z,W,Q,$){"use strict";var _=t(67971);E(U,"__esModule",{value:!0}),U.default=void 0,C=_(C),P=_(P),D=_(D),M=_(M),y=_(y),T=_(T),O=_(O),e=V(e),d=_(d),i=_(i),h=_(h),n=_(n);function j(l){if(typeof v!="function")return null;var u=new v,f=new v;return(j=function(R){return R?f:u})(l)}function V(l,u){if(!u&&l&&l.__esModule)return l;if(l===null||c(l)!=="object"&&typeof l!="function")return{default:l};var f=j(u);if(f&&f.has(l))return f.get(l);var p={},R=E&&S;for(var o in l)if(o!=="default"&&Object.prototype.hasOwnProperty.call(l,o)){var I=R?S(l,o):null;I&&(I.get||I.set)?E(p,o,I):p[o]=l[o]}return p.default=l,f&&f.set(l,p),p}var G=function(){var u=(0,W.useDispatch)(),f=(0,$.useQuery)("expertiseId"),p=(0,Q.useHistory)(),R=(0,e.useState)(!1),o=(0,y.default)(R,2),I=o[0],Y=o[1],k=(0,e.useState)(null),w=(0,y.default)(k,2),H=w[0],L=w[1],q=(0,W.useSelector)(function(s){return{expertiseInfo:s.expertises.expertiseInfo}}),g=q.expertiseInfo,ee=i.default.get(g,"basic_info.state",0);(0,e.useEffect)(function(){u.pageHeader.setNameSpace(!1),u.pageHeader.setTitle(e.default.createElement(d.default,null,"Drill experience details page")),u.pageHeader.setHeaderExtra(e.default.createElement("div",{style:{textAlign:"right"}},ee?e.default.createElement(M.default,{type:"primary",onClick:te},e.default.createElement(d.default,null,"create drill")):null)),u.pageHeader.showBackArrow(!0),u.pageHeader.setBreadCrumbItems(X.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"expertise_list",value:h.default.t("drill experience library"),path:"/chaos/expertise/list"},{key:"expertise_detail",value:h.default.t("Drill experience details page"),path:"/chaos/expertise/detail"}]))},[]),(0,e.useEffect)(function(){f&&(0,D.default)(regeneratorRuntime.mark(function s(){return regeneratorRuntime.wrap(function(b){for(;;)switch(b.prev=b.next){case 0:return b.next=2,u.expertises.getExpertiseDetail({expertise_id:f});case 2:case"end":return b.stop()}},s)}))()},[]);function te(){u.experimentEditor.setClearExperiment(),(0,Z.pushUrl)(p,"/chaos/experiment/editor",{expertiseId:f})}function ne(s){L(s),Y(!0)}function le(){L(null),Y(!1)}var a=i.default.get(g,"basic_info",null),z=i.default.get(g,"executable_info",null),K=i.default.get(g,"evaluation_info",{items:[]}),ae=i.default.get(z,"flow.runMode",""),F=i.default.get(z,"run_time","");return e.default.createElement("div",{className:n.default.warp},e.default.createElement("div",{className:n.default.baseInfo},e.default.createElement("div",{className:n.default.title},e.default.createElement(d.default,null,"Basic Information")),e.default.createElement("div",null,e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"Experience name")),e.default.createElement("div",{className:n.default.value,title:a&&a.name},a&&a.name)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"Experience description")),e.default.createElement("div",{className:n.default.value,title:a&&a.function_desc},a&&a.function_desc)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"Tag")),e.default.createElement("div",{className:n.default.value},!i.default.isEmpty(a)&&a.tags.map(function(s){return e.default.createElement(P.default,{type:"primary",size:"small",key:s,className:n.default.tagCss},s)}))),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"background")),e.default.createElement("div",{className:n.default.value,title:a&&a.background_desc},a&&a.background_desc)),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"Architectural Weaknesses")),e.default.createElement("div",{className:n.default.value,title:a&&a.design_concept},a&&a.design_concept)))),e.default.createElement("div",{className:n.default.flows},e.default.createElement("div",{className:n.default.title},e.default.createElement(d.default,null,"Exercise process")),e.default.createElement("div",{className:n.default.runEnvironment},e.default.createElement("div",{className:n.default.runTitle},e.default.createElement(d.default,null,"Operating environment")),e.default.createElement("div",null,!i.default.isEmpty(F)&&F.items.map(function(s){return e.default.createElement(P.default,{type:"primary",size:"small",key:s,className:n.default.tagCss},s)}))),e.default.createElement("div",null,!i.default.isEmpty(g)&&e.default.createElement(O.default,{isExpertise:!0,experiment:z,runMode:ae,onNodeClick:function(N){return ne(N)}}))),e.default.createElement("div",null,e.default.createElement("div",{className:n.default.title},e.default.createElement(d.default,null,"Other information")),e.default.createElement("div",{className:n.default.baseInfoItem},e.default.createElement("div",{className:n.default.label},e.default.createElement(d.default,null,"evaluating")),e.default.createElement("div",{className:n.default.value},!i.default.isEmpty(K)&&(0,C.default)(K.items).map(function(s,N){return e.default.createElement("li",{className:n.default.valueItem,key:N,title:s.desc},s.desc)})))),H&&e.default.createElement(T.default,{disabled:!0,readOnly:!0,isExpertise:!0,visible:I,data:H,onClose:le}))},J=G;U.default=J})},63030:(B,A,t)=>{"use strict";t.d(A,{Z:()=>v});var r=t(60994),x=t.n(r),m=t(93476),c=t.n(m),E=c()(x());E.push([B.id,`.index__warp__zTAyY .index__baseInfo__jIe1u {
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
}`],sourceRoot:""}]),E.locals={warp:"index__warp__zTAyY",baseInfo:"index__baseInfo__jIe1u",flows:"index__flows__ExfNM",title:"index__title__Hs68-",baseInfoItem:"index__baseInfoItem__TEz6H",label:"index__label__UTQof",value:"index__value__-hm27",tagCss:"index__tagCss__ecFuk",runEnvironment:"index__runEnvironment__TAvua",runTitle:"index__runTitle__j+yvR",valueItem:"index__valueItem__1pX4S"};const v=E},28790:(B,A,t)=>{"use strict";t.r(A),t.d(A,{default:()=>v});var r=t(1892),x=t.n(r),m=t(63030),c={};c.insert="head",c.singleton=!1;var E=x()(m.Z,c);const v=m.Z.locals||{}}}]);

//# sourceMappingURL=32.bundle.js.map