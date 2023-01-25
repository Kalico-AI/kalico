import React from 'react';

function Footer() {
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
                      <h4>Useful inks</h4>
                      <ul>
                        <li><a href="/about">About</a></li>
                        <li><a href="/contact">Contact</a></li>
                      </ul>
                    </div>
                  </div>
                  <div className="col-sm-6 col-md-4 wow fadeInUp" data-wow-delay="0.3s">
                    <div className="footer-menu">
                      <h4>Legal</h4>
                      <ul>
                        <li><a href="/tos">Terms of Service</a></li>
                        <li><a href="/privacy">Privacy Policy</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </footer>
  );
}

export default Footer;
