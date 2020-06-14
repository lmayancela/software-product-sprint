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

/**
 * Since addRandomGreeting and addFact are similar functions, I defined the greeting and facts arrays outside
 * these functions so that I can make a more general function to print out these statements. The arrays are defined below.
 */
const greetings =
    ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];
const facts = 
    ['I am actually a brother of the Gamma Chi chapter of the Sigma Alpha Mu Fraternity.','I have practiced playing both the Bass guitar and Ocarina.','I am studying Japanese and can speak it at an intermediate level (ぼくは日本に留学したい).','I never learned how to ride a bike.'];

/**
 * Adds a random greeting to the page.
 */
function addRandomText(arr) {
  // Pick a random greeting.
  const greeting = arr[Math.floor(Math.random() * arr.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Makes the element with id1 hidden and reveals the element with id2 in order to "go to a different page."
 * Params:
 * id1 - ID of the element to be hidden.
 * id2 - ID of the element to be un-hidden.
 */
function swapHidden(id1,id2) {
    const toBeHidden = document.getElementById(id1);
    const toBeRevealed = document.getElementById(id2);
    toBeRevealed.style.display = 'inline-block';
    toBeHidden.style.display = 'none';
}

/*Below is code that handles requests to the data servlet.*/

// Create the intro page using the JSON.
async function loadIntro() {
    const response = await fetch('/data');
    const data = await response.json();
    const intro = document.getElementById('welcome');
    const pageData = data[0];

    //Append the intro image.
    intro.appendChild(
        createImg(pageData[0])
    );

    //Append "Welcome to my Portfolio!"
    intro.appendChild(
        createH2(pageData[3])
    );

    //Append the p element that is on the intro page.
    intro.appendChild(
        createP(pageData[1])
    );
    
    //Append the button that navigates to the following page
    btn = createBtn(pageData[2]);
    intro.appendChild(btn);
    btn.onclick = function() {
        (swapHidden('welcome','intro-main'));
    };
    console.log("done3");
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
