import React from 'react';

function HeaderNav() {
  return (
      <header className="header-area">
        <nav className="navbar navbar-expand-lg menu_three sticky-nav">
          <div className="container-fluid">
            <a className="navbar-brand header_logo" href="/">
              <img className="main_logo" src="/assets/images/logo.png" alt="logo"/>
            </a>
            <button
                className="navbar-toggler collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarText"
                aria-controls="navbarText"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
            <span className="menu_toggle">
              <span className="hamburger">
                <span></span>
                <span></span>
                <span></span>
              </span>
              <span className="hamburger-cross">
                <span></span>
                <span></span>
              </span>
            </span>
            </button>
            <div className="collapse navbar-collapse justify-content-end" id="navbarText">
              <ul className="navbar-nav menu mx-auto">
                <li className="nav-item submenu mega-home active">
                  <a href="/" className="nav-link dropdown-toggle active">Home</a>
                </li>
                <li className="nav-item dropdown submenu active">
                  <a href="/#why-kalico" className="nav-link dropdown-toggle">Why Kalico</a>
                </li>
                <li className="nav-item dropdown submenu active">
                  <a href="/#how-it-works" className="nav-link dropdown-toggle">How It Works</a>
                </li>

                <li className="nav-item dropdown submenu mega-menu active">
                  <a href="/#features" className="nav-link dropdown-toggle">Features</a>
                </li>
                <li className="nav-item dropdown submenu mega-menu active">
                  <a href="/#support" className="nav-link dropdown-toggle">Support</a>
                </li>
                <li className="nav-item dropdown submenu mega-menu active">
                  <a href="/about" className="nav-link dropdown-toggle">About</a>
                </li>
                <li className="nav-item dropdown submenu mega-menu active">
                  <a href="/contact" className="nav-link dropdown-toggle">Contact Us</a>
                </li>
              </ul>
              <div className="right-nav">
                {/*<a href="#" className="language-bar mr-50"><span className="active">En.</span><span>Ru</span></a>*/}
                <a href="/account/sign-in">Sign in</a>
                <a className="btn btn-red" href="/account/sign-up">Sign Up</a>
              </div>
            </div>
          </div>
        </nav>
      </header>
  );
}

export default HeaderNav;
