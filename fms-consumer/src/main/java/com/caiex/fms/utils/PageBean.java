package com.caiex.fms.utils;

public class PageBean {
	 private int curPage;             //当前页  
	 private int pageCount;           //总页数  
	 private int rowsCount;           //总行数  
	 private int pageSize;         //每页多少行
	 
	 
	
	 
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getRowsCount() {
		return rowsCount;
	}
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	

	 public PageBean() {
		super();
	}



	public Integer getPage(int rows,int size){  
		 Integer pageCount=0;
		 
		 if(rows % size == 0){  
			 pageCount=rows / size;  
		  }else if(rows<size){  
		      pageCount=1;  
		  }else{  
		      pageCount=rows / size +1;  
		  } 
		 return pageCount;
	 }  
	
	
	/* public PageBean(int rows){  
    
	   this.setRowsCount(rows);  
	   if(this.rowsCount % this.pageSize == 0){  
	          this.pageCount=this.rowsCount / this.pageSize;  
	    }else if(rows<this.pageSize){  
	         this.pageCount=1;  
	    }else{  
	          this.pageCount=this.rowsCount / this.pageSize +1;  
	     }  
}  */


}
