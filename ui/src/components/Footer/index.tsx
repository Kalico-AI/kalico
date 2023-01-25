import React from 'react';

function Footer() {
  return (
      <footer className="footer-pos footer-software footer-hrm bg-aqua pt-120">
        {/*<div className="bg-shapes">*/}
        {/*  <div className="shape">*/}
        {/*    <img data-parallax='{"x":100, "y": -200, "rotateY":0}'*/}
        {/*         src="/assets/images/home_5/icons/kite.svg" alt="Shape"/>*/}
        {/*  </div>*/}
        {/*  <div className="shape">*/}
        {/*    <img data-parallax='{"x":100, "y": 0, "rotateY":0}'*/}
        {/*         src="/assets/images/home_5/icons/women.svg" alt="Shape"/>*/}
        {/*  </div>*/}
        {/*  <div className="shape">*/}
        {/*    <img data-parallax='{"x":100, "y": 0, "rotateY":0}'*/}
        {/*         src="/assets/images/home_5/icons/leaf.svg" alt="Shape"/>*/}
        {/*  </div>*/}
        {/*</div>*/}
        {/*<div className="footer-top">*/}
          {/*<div className="container">*/}
          {/*  <div className="row">*/}
          {/*    <div className="col-lg-12">*/}
          {/*      <div className="row justify-content-between">*/}
          {/*        <div className="col-sm-6 col-md-4 col-lg-3 wow fadeInUp" data-wow-delay="0.1s">*/}
          {/*          <div className="footer-menu">*/}
          {/*            <h4>Company</h4>*/}
          {/*            <ul>*/}
          {/*              <li><a href="about.html">About</a></li>*/}
          {/*              <li><a href="career.html">Careers</a></li>*/}
          {/*              <li><a href="contact.html">Contact</a></li>*/}
          {/*              <li><a href="blog.html">Blog</a></li>*/}
          {/*              <li><a href="help-center.html">Support</a></li>*/}
          {/*            </ul>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*        <div className="col-sm-6 col-md-4 col-lg-3 wow fadeInUp" data-wow-delay="0.2s">*/}
          {/*          <div className="footer-menu">*/}
          {/*            <h4>Useful inks</h4>*/}
          {/*            <ul>*/}
          {/*              <li><a href="about.html">About</a></li>*/}
          {/*              <li><a href="career.html">Careers</a></li>*/}
          {/*              <li><a href="contact.html">Contact</a></li>*/}
          {/*              <li><a href="blog.html">Blog</a></li>*/}
          {/*              <li><a href="help-center.html">Support</a></li>*/}
          {/*            </ul>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*        <div className="col-sm-6 col-md-4 col-lg-3 wow fadeInUp" data-wow-delay="0.3s">*/}
          {/*          <div className="footer-menu">*/}
          {/*            <h4>Products</h4>*/}
          {/*            <ul>*/}
          {/*              <li><a href="about.html">About</a></li>*/}
          {/*              <li><a href="career.html">Careers</a></li>*/}
          {/*              <li><a href="contact.html">Contact</a></li>*/}
          {/*              <li><a href="help-center.html">Support</a></li>*/}
          {/*            </ul>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*        <div className="col-sm-6 col-md-4 col-lg-3 wow fadeInUp" data-wow-delay="0.5s">*/}
          {/*          <div className="footer-menu">*/}
          {/*            <h4>Newsletter</h4>*/}
          {/*            <p className="newsletter-text">Sign up and receive the latest tips via*/}
          {/*              email.</p>*/}
          {/*            <div className="newsletter-box mt-25">*/}
          {/*              <p className="hrm-email">Write you email*</p>*/}
          {/*              <form action="#">*/}
          {/*                <div className="form-group mb-10">*/}
          {/*                  <i className="icon_mail_alt"></i>*/}
          {/*                  <input type="text" className="form-control hrm-input"*/}
          {/*                         placeholder="Your email:"/>*/}
          {/*                </div>*/}
          {/*                <button type="submit" className="btn">Subscribe</button>*/}
          {/*              </form>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*    </div>*/}
          {/*  </div>*/}
          {/*</div>*/}
        {/*</div>*/}
        <div className="container">
          <div className="footer-bottom wow fadeInUp" data-wow-delay="0.1s">
            <div className="row align-items-center">
              <div className="col-lg-3 text-sm-center text-md-start">
                <ul>
                  <li className="footer_logo">
                    <a href="#">
                      <img className="d-md-block d-sm-inline-block"
                           src="/assets/images/logo.png" alt="Footer Logo"/></a>
                  </li>
                </ul>
              </div>
              <div className="col-lg-6">
                <div className="footer-bottom-menu">
                  <ul className="">
                    <li><a href="#">Terms of Use</a></li>
                    <li><a href="#">Privacy</a></li>
                    <li><a href="#">About</a></li>
                  </ul>
                </div>
              </div>
              <div className="col-lg-3">
                <ul className="social-link-bg-2">
                  <li>
                    <a href="#"><i className="fab fa-facebook-f"></i></a>
                  </li>
                  <li>
                    <a href="#"><i className="fab fa-twitter"></i></a>
                  </li>
                  <li>
                    <a href="#"><i className="fab fa-linkedin-in"></i></a>
                  </li>
                  <li>
                    <a href="#"><i className="fab fa-instagram"></i></a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </footer>
  );
}

export default Footer;
