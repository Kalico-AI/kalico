import React, {useEffect, useState} from 'react';
import {useAuthUser, withAuthUser} from "next-firebase-auth";
import {useRouter} from "next/router";
import {PATHS} from "@/utils/constants";
import dynamic from "next/dynamic";

const Footer = () => {
  const user = useAuthUser()
  const router = useRouter()
  const [hide, setHide] = useState(true)

  useEffect(() => {
    const interval = setInterval(() => {
      setHide(false)
    }, 2000)
    if (user.id) {
      setHide(false)
    }
    return () => {
      clearInterval(interval)
    }
  }, [user.id])

  if (user && user.id && router.pathname.includes(PATHS.DASHBOARD) || hide) {
    return <></>
  }

  return (
      <footer className="footer-pos footer-software footer-hrm bg-aqua">
        <div className="footer-top">
          <div className="container">
            <div className="row">
              <div className="col-lg-12">
                <div className="row justify-content-between">
                  <div className="col-sm-6 col-md-4 wow fadeInUp" data-wow-delay="0.1s">
                    <div className="footer-menu">
                      <div className="footer_logo">
                        <a href="/">
                          <img className="d-md-block d-sm-inline-block"
                               src="/assets/images/logo.png" alt="Footer Logo"/></a>
                      </div>
                      <p>Copyright &copy; 2023 Kalico, Inc.</p>
                      <p>All Rights Reserved</p>
                    </div>
                  </div>
                  <div className="col-sm-6 col-md-4 wow fadeInUp" data-wow-delay="0.2s">
                    <div className="footer-menu">
                      <h4>Getting in Touch</h4>
                      <ul className="social-link-bg-2">
                        <li>
                          <a href="#"><i className="fab fa-facebook-f"></i></a>
                        </li>
                        <li>
                          <a href="#"><i className="fab fa-twitter"></i></a>
                        </li>
                        <li>
                          <a href="#"><i className="fab fa-instagram"></i></a>
                        </li>
                      </ul>
                    </div>
                  </div>
                  {/*<div className="col-sm-6 col-md-4 wow fadeInUp" data-wow-delay="0.3s">*/}
                  {/*  <div className="footer-menu">*/}
                  {/*    <h4>Legal</h4>*/}
                  {/*    <ul>*/}
                  {/*      <li><a href="/legal/tos">Terms of Service</a></li>*/}
                  {/*      <li><a href="/legal/privacy">Privacy Policy</a></li>*/}
                  {/*    </ul>*/}
                  {/*  </div>*/}
                  {/*</div>*/}
                </div>
              </div>
            </div>
          </div>
        </div>
      </footer>
  );
}
export default dynamic(() => Promise.resolve(withAuthUser()(Footer)), {
  ssr: false
})

