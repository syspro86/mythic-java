// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.18.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.18.0/firebase-analytics.js";
import { getAuth, onAuthStateChanged, signInWithPopup, signInWithRedirect, getRedirectResult, GoogleAuthProvider } from "https://www.gstatic.com/firebasejs/9.18.0/firebase-auth.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

export default {
    data() {
        return {
            'app': null,
            'analytics': null,
            'auth': null,
            'provider': null,
            'user': null
        }
    },
    methods: {
        login(event) {
            signInWithRedirect(this.auth, this.provider);
        }
    },
    mounted() {
        const getCookie = name => {
            let matches = document.cookie.match(new RegExp(
              "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
            ));
            return matches ? decodeURIComponent(matches[1]) : undefined;
        }
          
        this.user = getCookie('uid');
        if (this.user !== undefined) {
            return;
        }

        const firebaseConfig = {
            apiKey: "AIzaSyADalxNmCg4MkfyxcfBWna5jldav-TCtL8",
            authDomain: "mythic-ab460.firebaseapp.com",
            databaseURL: "https://mythic-ab460-default-rtdb.asia-southeast1.firebasedatabase.app",
            projectId: "mythic-ab460",
            storageBucket: "mythic-ab460.appspot.com",
            messagingSenderId: "19493259164",
            appId: "1:19493259164:web:82918d404f0d767da96a59",
            measurementId: "G-MN7N8DTBTT"
        };
    
        this.app = initializeApp(firebaseConfig);
        this.analytics = getAnalytics(this.app);
        this.auth = getAuth(this.app);
        this.provider = new GoogleAuthProvider();

        onAuthStateChanged(this.auth, user => {
            console.log(user);
            if (user) {
                this.user = user.uid;
                document.cookie = 'uid=' + encodeURIComponent(this.user);
            }
        });
    },
    template: `
    <v-btn variant="outlined" v-if="user == null" @click="login" type="outlined">로그인</v-btn>
    `
}
