import React from 'react';

function Footer() {
  return (
      <footer className="footer-area bg-black">
        <div className="footer-top">
          <div className="container">
            <div className="row">
              <div className="col-lg-4">
                <div className="footer-content">
                  <span>The Most Trusted Online Course Platform for WordPress.</span>
                  <img src="/assets/images/logo-3.png" alt="Footer Logo"/>
                </div>
              </div>
              <div className="col-lg-8">
                <div className="row justify-content-between">
                  <div className="col-sm-6 col-md-4">
                    <div className="footer-menu">
                      <h4>Features</h4>
                      <ul>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                      </ul>
                    </div>
                  </div>
                  <div className="col-sm-6 col-md-4">
                    <div className="footer-menu">
                      <h4>Company</h4>
                      <ul>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                      </ul>
                    </div>
                  </div>
                  <div className="col-sm-6 col-md-4">
                    <div className="footer-menu">
                      <h4>Support</h4>
                      <ul>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                        <li><a href="#">Design</a></li>
                        <li><a href="#">Social media</a></li>
                        <li><a href="#">Develop</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="footer-bottom">
            <div className="row align-items-center">
              <div className="col-lg-5">
                <ul>
                  <li><img src="/assets/images/logo-2.png" alt="Logo Image"/></li>
                  <li>
                    <span className="copyright">Copyright 2021, All Rights Reserved</span>
                  </li>
                </ul>
              </div>
              <div className="col-lg-5">
                <div className="footer-bottom-menu">
                  <ul>
                    <li><a href="#">Terms & Conditions</a></li>
                    <li><a href="#">Privacy Policy</a></li>
                    <li><a href="#">Legal Notice</a></li>
                  </ul>
                </div>
              </div>
              <div className="col-lg-2">
                <ul className="social-link-bg">
                  <li>
                    <a href="#"><i className="fab fa-facebook-f"></i></a>
                  </li>
                  <li>
                    <a href="#"><i className="fab fa-twitter"></i></a>
                  </li>
                  <li>
                    <a href="#"><i className="fab fa-google-plus-g"></i></a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <span className="footer-shape"></span>
      </footer>
  );
}

export default Footer;
