import React from 'react';


function Features() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="features section-padding">
        <div className="container">
          <div className="row">
            <div className="col-lg-6">
              <div className="features-image">
                <img src="/assets/images/features/feature.png" alt="Features Image"/>
                <img
                    src="/assets/images/features/features-object-1.svg"
                    alt="Features Object"
                    className="features-object1 object-element"
                    data-paroller-factor="0.1"
                    data-paroller-type="foreground"
                    data-paroller-direction="horizontal"
                    data-paroller-transition="transform .2s linear"
                />
                <img src="/assets/images/features/features-object-2.svg" alt="Features Object"
                     className="features-object2"/>
                <img
                    className="features-object3 object-element"
                    data-paroller-factor="0.05"
                    data-paroller-type="foreground"
                    data-paroller-direction="horizontal"
                    data-paroller-transition="transform .2s linear"
                    src="/assets/images/features/features-object-3.svg"
                    alt="Features Object"
                />
                <img
                    src="/assets/images/features/features-object-4.svg"
                    alt="Features Object"
                    className="features-object4 object-element"
                    data-paroller-factor="0.05"
                    data-paroller-type="foreground"
                    data-paroller-transition="transform .2s linear"
                />
                <img
                    src="/assets/images/features/features-object-5.svg"
                    alt="Features Object"
                    className="features-object5 object-element"
                    data-paroller-type="foreground"
                    data-paroller-factor="0.1"
                    data-paroller-transition="transform .2s linear"
                />
                <div className="round-object"></div>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="features-content">
                <h2>Outstanding Features & Automations</h2>
                <h3>
                  We created world's first project visualisation software for
                  <span>remote teams.</span>
                </h3>
                <p>
                  We're here every step of the way making sure you and your team deliver. We're
                  here every step of the way making sure you and your team deliver
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Features;
