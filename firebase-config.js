// firebase-config.js
const firebaseConfig = {
    apiKey: "AIzaSyCgB3Gi-KbzhOln4F5RQGQbb1d2Lx2Y5EQ",
    authDomain: "empire-house-productions.firebaseapp.com",
    projectId: "empire-house-productions",
    storageBucket: "empire-house-productions.firebasestorage.app",
    messagingSenderId: "491578812797",
    appId: "1:491578812797:web:57e61edb7e7d6ff3a60414",
    measurementId: "G-2N04H460SP"
};

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();
const db = firebase.firestore();