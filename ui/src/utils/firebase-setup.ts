import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const devConfig = {
  apiKey: 'AIzaSyBPK8HxA8Ul-RDAVcrxdDsFL44mn4aUkR8',
  authDomain: 'foodwallah-dev.firebaseapp.com',
  projectId: 'foodwallah-dev',
  storageBucket: 'foodwallah-dev.appspot.com',
  messagingSenderId: '668448388734',
  appId: '1:668448388734:web:d80ccea21f8c2309318a2f',
  measurementId: 'G-BRLSYWQK53'
}


const prodConfig = {
  apiKey: "AIzaSyAKB1lxHNctFnQOHtUdpZqV5bn9gGv1dAU",
  authDomain: "kalico-ai.firebaseapp.com",
  projectId: "kalico-ai",
  storageBucket: "kalico-ai.appspot.com",
  messagingSenderId: "635275971140",
  appId: "1:635275971140:web:28053177fc713f3d88ca11",
  measurementId: "G-XDVFQRG710"
};

// Initialize Firebase
const app = initializeApp(process.env.NODE_ENV === "production" ? prodConfig : devConfig);
export const auth = getAuth(app);