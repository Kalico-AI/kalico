import React from 'react';
import Hero from "@/pages/RecipeLanding/Hero";
import Search from "@/pages/RecipeLanding/Search";
import TopRecipes from "@/pages/RecipeLanding/TopRecipes";

function RecipeLanding() {
  return (
      <>
        <main>
          <Hero/>
          <Search/>
          <TopRecipes/>
          {/*<VideoDemo/>*/}
          {/*<WhyKalico/>*/}
          {/*<HowItWorks/>*/}
        </main>
      </>
  );
}

export default RecipeLanding;
