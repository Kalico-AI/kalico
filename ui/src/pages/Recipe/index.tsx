import React, {FC} from 'react';
import Hero from "@/pages/Recipe/Hero";
import Search from "@/pages/Recipe/Search";
import TopRecipes from "@/pages/Recipe/TopRecipes";
import {IndexProps} from "../../../pages";


const RecipeLanding: FC<IndexProps> =  (props) => {
  return (
      <>
        <main>
          <Hero/>
          <Search/>
          <TopRecipes posts={props.posts}/>
          {/*<VideoDemo/>*/}
          {/*<WhyKalico/>*/}
          {/*<HowItWorks/>*/}
        </main>
      </>
  );
}

export default RecipeLanding;
