import React from 'react';


function WhyKalico() {
  return (
      <section className="hardware-support-area pt-150 pb-150">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6">
              <div className="section-title-left">
                <h2>Why use Kalico?</h2>
              </div>

              <p className="hardware-text">
                Some point of sale systems lock you into their hardwares devices. This may not best
                suit your needs or budget. Landpagy POS allows you to choose
                the best hardware for your needs.
              </p>

              <div className="row">
                <div className="col-md-6">
                  <ul className="hardware-list">
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon1.svg" alt="Icon"
                             className="hardware-icon"/>
                        Printers
                      </p>
                    </li>
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon2.svg" alt="Icon"
                             className="hardware-icon"/>
                        Customer Display
                      </p>
                    </li>
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon3.svg" alt="Icon"
                             className="hardware-icon"/>
                        Payment Terminals
                      </p>
                    </li>
                  </ul>
                </div>
                <div className="col-md-6">
                  <ul className="hardware-list">
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon4.svg" alt="Icon"
                             className="hardware-icon"/>
                        Cash Drawer
                      </p>
                    </li>
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon5.svg" alt="Icon"
                             className="hardware-icon"/>
                        Barcode Scanner
                      </p>
                    </li>
                    <li>
                      <p>
                        <img src="/assets/images/home_5/icons/hardware-icon6.svg" alt="Icon"
                             className="hardware-icon"/>
                        NFC / RFID Readers
                      </p>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div className="col-lg-5 offset-lg-1">
              <div className="available">
                <div>
                  <h4 className="item-title">Available now</h4>
                </div>
                <div className="app-wrapp">
                  <ul>
                    <li>
                      <a href="#">
                        <img src="/assets/images/home_5/icons/apple-icon.svg" alt="Icon"
                             className="app-icon"/>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <img src="/assets/images/home_5/icons/android-icon.svg" alt="Icon"
                             className="app-icon"/>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <img src="/assets/images/home_5/icons/windows-icon.svg" alt="Icon"
                             className="app-icon"/>
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default WhyKalico;
