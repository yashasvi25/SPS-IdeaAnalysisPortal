// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
google.charts.load('current', {'packages':['corechart']});
// google.charts.setOnLoadCallback(drawChart);

/** Creates a chart and adds it to the page. */
function voteGraph() {

  var productid=localStorage.getItem("productid")
  fetch('/vote-response?productid='+productid  ).then(response => response.json())
  .then((obj) => {
      
    var data = new google.visualization.DataTable();
   
    data.addColumn('string', 'Upvote-Downvote');
    data.addColumn('number', 'Votes');
    data.addRow(["Upvotes",obj["upvotes"]]);
    data.addRow(["Downvotes",obj["downvotes"]]);

    const options = {
      'title': 'Vote Statistics',
      'width':600,
      'height':500,
      sliceVisibilityThreshold: 0
    };

    const chart = new google.visualization.PieChart(document.getElementById('voteGraph-container'));
    chart.draw(data, options);
    sentimentGraph();
  });
}

function sentimentGraph() {

  var productid=localStorage.getItem("productid")
  fetch('/sentiment-score?productid='+productid  ).then(response => response.json())
  .then((obj) => {
    


    var countArr = [0,0,0,0,0];
    for(var i=0;i<obj.length;i++){
        var value = obj[i]["sentimentAnalysisScore"];
        if(value==10)
            countArr[4]+=1;
        else
            countArr[Math.floor(value/2)]+=1;
        console.log(Math.floor(value/2));
        console.log(countArr[Math.floor(value/2)]);
    }  

    var data = new google.visualization.DataTable();
   
    data.addColumn('string', 'Percentage-Positiveness');
    data.addColumn('number', 'Number of Reviews');
    data.addRow(["Very Poor",countArr[0]]);
    data.addRow(["Poor",countArr[1]]);
    data.addRow(["Average",countArr[2]]);
    data.addRow(["Good",countArr[3]]);
    data.addRow(["Very Good",countArr[4]]);

    const options = {
      'title': 'Comments Sentiment Analysis Statistics',
      'width':600,
      'height':500,
      sliceVisibilityThreshold: 0
    };

    const chart = new google.visualization.PieChart(document.getElementById('sentimentGraph-container'));
    chart.draw(data, options);
    agegroupGraph(); 
  });
}

function agegroupGraph() {

  var productid=localStorage.getItem("productid")
  fetch('/age-count?productid='+productid  ).then(response => response.json())
  .then((obj) => {
    var data = new google.visualization.DataTable();
   
    data.addColumn('string', 'Age Group Count');
    data.addColumn('number', 'Votes');
    data.addRow(["0-14",obj["ageGroupCount"][0]]);
    data.addRow(["15-24",obj["ageGroupCount"][1]]);
    data.addRow(["25-65",obj["ageGroupCount"][2]]);
    data.addRow([">65",obj["ageGroupCount"][3]]);

    const options = {
      'title': 'Suitable Age Group Survey Statistics',
      'width':600,
      'height':500,
      sliceVisibilityThreshold: 0
    };

    const chart = new google.visualization.PieChart(document.getElementById('agegroupGraph-container'));
    chart.draw(data, options);
  });
}

function getCommentPara(comment){
    var commentPara = document.createElement("p");
    var commentText = document.createTextNode(comment.suggestion);
    commentPara.appendChild(commentText);
    return commentPara;
}

function getCommentData(comment){
    var blockQuote = document.createElement("blockquote");
    blockQuote.className = "blockquote mb-0";
    blockQuote.appendChild(getCommentPara(comment));
    // blockQuote.appendChild(getCommentFooter(comment));
    return blockQuote;
}

function getCommentDiv(comment){
    var commentDiv = document.createElement("div");
    commentDiv.className = "commentbackground";
    commentDiv.appendChild(getCommentData(comment));    
    return commentDiv;
}

function addSuggestions() {
  productid = localStorage.getItem("productid");
  console.log(productid);
  fetch('/ideaComments?productid=' + productid).then(response => response.json()).then((comments) => {

    var commentsContainer = document.getElementById('suggestions-container');
    for(var i = 0; i < comments.length; i++){
        var commentDiv = getCommentDiv(comments[i]);
        var lineBreak = document.createElement("br");

        var ulist = document.createElement("ul");
        ulist.className="list-group";
        var temp=comments[i].keyWords;
        for(var j = 0; j < temp.length; j++){
            var litem = document.createElement("li");
            litem.className="list-group-item";
            litem.innerHTML = temp[j];
            ulist.appendChild(litem);
        }
        var containerfluid = document.createElement('div');
        containerfluid.className='container-fluid';
        var rowdiv = document.createElement('div');
        rowdiv.className='row';

        var col1div = document.createElement('div');
        col1div.className='col-md-8';
        col1div.appendChild(commentDiv);

        var col2div = document.createElement('div');
        col2div.className='col-md-4';
        col2div.appendChild(ulist);

        rowdiv.appendChild(col1div);
        rowdiv.appendChild(col2div);
        containerfluid.appendChild(rowdiv);

        commentsContainer.appendChild(containerfluid);
        commentsContainer.appendChild(lineBreak);
    }
    voteGraph();


  });
}
