import React from 'react';


function VideoDemo() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="app-video-area">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <div className="app-video-wrapp">
                <a className="play-btn" data-fancybox=""
                   href="https://www.youtube.com/watch?v=eNrCEqucHb4">
                  <i className="fas fa-play"></i>
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default VideoDemo;
