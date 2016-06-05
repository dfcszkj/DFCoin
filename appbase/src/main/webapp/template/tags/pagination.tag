<style type="text/css">
	.page_nav a{border:1px solid #CDE6FF;margin:2px;padding:2px 6px;background-color:#F2F9FD;}
	.page_nav span{border:1px solid #CDE6FF;margin:2px;padding:2px 6px; }
	.page_nav a:hover{background-color:#E4F2FF;text-decoration: none;}
</style>
<input type="hidden" name="pageNumber" value="${page.pageNumber!1}" id="pageNumber" />
<input type="hidden" name="pageSize" value="${page.pageSize!15}" id="pageSize" />
<?
	DIRECTIVE SAFE_OUTPUT_OPEN;
 	if(page != null){
?>
<div class="row" style="  margin: 0 !important; background-color: #eff3f8;">
	<div class="col-xs-6" style="margin-top:9px;">
		<div class="page_nav" id="page_nav">
			Page ${page.totalPage == 0 ? 0 : page.pageNumber} of ${page.totalPage!0}　Total ${page.totalRow!0} entries
			<!-- &nbsp;&nbsp; PageSize: <a href="javascript:void(0);">15</a><a href="javascript:void(0);">25</a><a href="javascript:void(0);">50</a> -->
		</div>
	</div>
	<div class="col-xs-6">
		<div class="dataTables_paginate paging_bootstrap">
			<ul class="pagination">
				<li class="first prev disabled" title="index"><a id="first" href="javascript:void(0);"><i class="fa fa-angle-double-left"></i></a></li>
				<li class="prev disabled" title="pre"><a id="prev" href="javascript:void(0);"><i class="fa fa-angle-left"></i></a></li>
					<?
							var pageNumber = page.pageNumber;
							if( page.totalPage > 10){ //总页数大于10
								if( pageNumber > 10 ){  //当前页大于10
									for(var i = ( pageNumber - 10 ); i < pageNumber; i++){
					?>
										<li class="page-index"><a href="javascript:void(0);" pageindex="${ i + 1 }">${ i + 1 }</a></li>
					<?		
									}
								}else{ //当前页小于10
									for(var i = 0; i < pageNumber; i++){
					?>
									<li class="page-index"><a href="javascript:void(0);" pageindex="${ i + 1 }">${ i + 1 }</a></li>
					<?				
									}
								}
							}else{
					?>
				<?for(var i=0;i<page.totalPage;i++) {?>
					<li class="page-index"><a href="javascript:void(0);" pageindex="${ i + 1 }">${ i + 1 }</a></li>
				<? 
				}
						} 
				?>		
				<li class="next"><a id="next" href="javascript:void(0);" title="下一页"><i class="fa fa-angle-right"></i></a></li>
				<li class="next last"><a id="last" href="javascript:void(0);" title="尾页"><i class="fa fa-angle-double-right last"></i></a></li>
			</ul>
		</div>
	</div>
</div>
<?
	}
	DIRECTIVE SAFE_OUTPUT_CLOSE;	
?>
<script>
	jQuery(function(){
		var currentPage = "${page.pageNumber!0}";  //当前页
		var totalPage = "${page.totalPage!0}";   //共有几页
		var pageSize = "${page.pageSize!15}";    //每页默认有15条数据
		
		initPagination(pageSize,currentPage,totalPage);
		
		//初始化分页器
		function initPagination(pageSize,currentPage,totalPage){
			 activePage(currentPage,totalPage);        //设置当前页
			 setPageSize();							   //设置每页显示多少条记录
			 activePageSize(pageSize);
			 $(".page-index a").click(pageClick);      //点击页码翻页
			 $("#first").click(firstPageClick);        //点击首页
			 $("#prev").click(prePageClick);           //点击上一页
			 $("#next").click(nextPageClick);          //点击下一页
			 $("#last").click(lastPageClick);          //尾页
		}
		
		//设置当前页
		function activePage(currentPage,totalPage){
			currentPage = parseInt(currentPage);
			totalPage = parseInt(totalPage);
			
			if( (currentPage - 1) <= totalPage && currentPage >= 0){
				var $active = $(".pagination a[pageindex='" + currentPage +"']").parent("li");
				
				if(currentPage > 1 ){
					$(".prev,.next").removeClass("disabled");
				}
				
				if(currentPage == totalPage){
					$(".next").addClass("disabled");
				}
				
				if(currentPage == 1){
					$(".prev").addClass("disabled");
				}
				
				$active.addClass("active");
				
			}
		}
		
		//使每页显示条数的元素变色
		function activePageSize(pageSize){
			$("#page_nav > a").each(function(i,v){
				$.trim($(this).html()) == pageSize ? $(this).css("background-color","#E4F2FF") : pageSize;
			});
		}
		
		//点击翻页 
		function pageClick(e,pageIndex){
			if(!pageIndex){
				pageIndex = $(this).attr("pageindex");
			}
			$("#pageNumber").val(pageIndex);
			$("#${formId}").submit();
		}
		
		//下一页
		function nextPageClick(){
			if(parseInt(currentPage) < parseInt(totalPage)){
				pageClick(null,parseInt(currentPage) + 1);
			}
		}
		
		//末页
		function lastPageClick(){
			pageClick(null,parseInt(totalPage));
		}
		
		//上一页
		function prePageClick(){
			if(parseInt(currentPage) > 1){
				pageClick(null,parseInt(currentPage) - 1);
			}
		}
		
		//首页
		function firstPageClick(){
			pageClick(null, 1 );
		}
		
		/**
		* 设置每页显示多少条记录 
		*/
		function setPageSize(){
			$("#page_nav > a").on("click",function(){
				var pageSize = $.trim($(this).html());
				if(pageSize.length == 0){
					pageSzie = 15;
				}
				$("#pageSize").val(pageSize);
				//set pageNumber to 1
				$("#pageNumber").val(1);
				$("#${formId}").submit(); //页面跳转
			});
		}
		
		/**
		* 翻页重载数据
		* @param pageSize   	每页多少条数据
		* @param currentPage    当前页
		* @param queryObj       查询条件
		* @param url            查询地址      
		*/
		function reloadData(pageSize,currentPage,queryObj,url){
			
		}
		
	});
</script>