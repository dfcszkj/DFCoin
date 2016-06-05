$(document).ready(function(){
  $("#dhtc01,#dhtc02,#dhtc03,#dhtc04,#dhtc05,#dhtc06,#dhtc07").hide();
  
  $("#dh01").mouseover(function(){
  $("#dhtc01").show(200);
  $("#dhtc02,#dhtc03,#dhtc04,#dhtc05,#dhtc06,#dhtc07").hide();
  });
  $("#dhtc01").mouseleave(function(){
  $("#dhtc01").hide(200);
  });
  
  $("#dh02").mouseover(function(){
  $("#dhtc02").show(200);
  $("#dhtc01,#dhtc03,#dhtc04,#dhtc05,#dhtc06,#dhtc07").hide();
  });
  $("#dhtc02").mouseleave(function(){
  $("#dhtc02").hide(200);
  });
  
  $("#dh03").mouseover(function(){
  $("#dhtc03").show(200);
  $("#dhtc01,#dhtc02,#dhtc04,#dhtc05,#dhtc06,#dhtc07").hide();
  });
  $("#dhtc03").mouseleave(function(){
  $("#dhtc03").hide(200);
  });

  $("#dh04").mouseover(function(){
  $("#dhtc04").show(200);
  $("#dhtc01,#dhtc02,#dhtc03,#dhtc05,#dhtc06,#dhtc07").hide();
  });
  $("#dhtc04").mouseleave(function(){
  $("#dhtc04").hide(200);
  });
  
  $("#dh05").mouseover(function(){
  $("#dhtc05").show(200);
  $("#dhtc01,#dhtc02,#dhtc03,#dhtc04,#dhtc06,#dhtc07").hide();
  });
  $("#dhtc05").mouseleave(function(){
  $("#dhtc05").hide(200);
  });
  
  $("#dh06").mouseover(function(){
  $("#dhtc06").show(200);
  $("#dhtc01,#dhtc02,#dhtc03,#dhtc04,#dhtc05,#dhtc07").hide();
  });
  $("#dhtc06").mouseleave(function(){
  $("#dhtc06").hide(200);
  });
  
  $("#dh07").mouseover(function(){
  $("#dhtc07").show(200);
  $("#dhtc01,#dhtc02,#dhtc0,#dhtc04,#dhtc05,#dhtc06").hide();
  });
  $("#dhtc07").mouseleave(function(){
  $("#dhtc07").hide(200);
  });
  
  $("#dh08").mouseover(function(){
  $("#dhtc08").show(200);
  $("#dhtc01,#dhtc02,#dhtc0,#dhtc04,#dhtc05,#dhtc06").hide();
  });

});
