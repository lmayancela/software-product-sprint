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

//This variable will keep track of the current page that is being displayed.
var currPage = "";
var currView = "content";

/**
 * Makes the element stored in currPage hidden and reveals the element with id in order to "go to a different page."
 * Params:
 * id - ID of the element to be un-hidden.
 */
function swapHidden(id) {
    if(id != currPage){
        const toBeHidden = document.getElementById(currPage);
        const toBeRevealed = document.getElementById(id);
        toBeRevealed.style.display = 'inline-block';
        toBeHidden.style.display = 'none';
        currPage = id;
    }
}

/**
 * Handles swapping between portfolio and comment views
 */
function swapView() {
    if(currView == "content") {
        document.getElementById(currView).style.display = 'none';
        document.getElementById('comment-content').style.display = 'inline-block';
        currView = "comment-content";
    } else {
        console.log("1");
        document.getElementById(currView).style.display = 'none';
        console.log("2");
        document.getElementById('content').style.display = 'inline-block';
        console.log("3");
        currView = "content";
    }
}

//Returns an array containing every comment made.
function getComments(params) {
    const data = document.getElementsByClassName("comment-style");
    var i;
    for(i=0; i<data.length; i++) {
        const comment = data[i].children[1].innerText;
        params.append("comments", comment);
    }
}

//Replaces old comments with those of the new language.
function postNewComments(data) {
    const comments = document.getElementsByClassName("comment-style");
    var i;
    for(i=0; i<data.length; i++) {
        const comment = comments[i].children[1];
        comment.innerHTML = data[i];
    }
}

/*Below is code that handles requests to the data servlet.*/

// On page load, the data will be fetched specific parts will be passed into the various load functions that must run.
async function load() {
    const response = await fetch('/data');
    const data = await response.json();
    loadComments(data[1][0]);
    currPage = "landing";
}

async function loadComments(commentData) {
    const commentSection = document.getElementById('comment-box');
    for(i=0; i<commentData.length; i++) {
        const comment = commentData[i].comment;
        const author = commentData[i].username;
        const wrapper = document.createElement('div');
        wrapper.classList.add("comment-style");
        wrapper.appendChild(
            createH2(author)
        );
        wrapper.appendChild(
            createP(comment)
        );
        commentSection.appendChild(wrapper);
    }
}

async function translate() {
    //Get the language code from what the user selected.
    const languageCode = document.getElementById('language').value;

    //Define parameters for the request.
    const params = new URLSearchParams();
    params.append('languageCode', languageCode);
    getComments(params);

    //Make the fetch request 
    const response = await fetch('/translate', {
      method: 'POST',
      body: params
    });
    const data = await response.json();
    
    //Post the new text
    postNewComments(data);
}

//Creates an h2 element with the given parameters
function createH2(text) {
    const h2Element = document.createElement('h2');
    h2Element.innerText = text;
    return h2Element;
}

//Creates a p element with the given parameters
function createP(text) {
    const pElement = document.createElement('p');
    pElement.innerText = text;
    return pElement;
}

//Creates a button element with the given parameters
function createBtn(text) {
    const btn = document.createElement('button');
    btn.innerText = text;
    return btn;
}

//Creates an image with the given parameters
function createImg(src) {
    const img = document.createElement('img');
    img.src = src;
    return img;
}
/**
 * Prints the welcome statement from the servlet.
 * This code will be commented out in favor of a function that will parse the JSON received from the servlet.
 * Like the case in the DataServlet.java file, this code is in a current PR and I am unsure as to whether it
 * is okay to fully remove this code until its PR is merged.
 */
// function getWelcome() {
//     fetch('/data').then(response => response.text()).then((welcome) => {
//     document.getElementById('welcome-container').innerText = welcome;
//   });
// }
