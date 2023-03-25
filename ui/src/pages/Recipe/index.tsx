import React, {FC} from 'react';
import Hero from "@/pages/Recipe/Hero";
import Search from "@/pages/Recipe/Search";
import MostRecentRecipes from "@/pages/Recipe/MostRecentRecipes";
import {IndexProps} from "../../../pages";


const RecipeLanding: FC<IndexProps> =  (props) => {
  return (
      <>
        <main>
          <Hero/>
          <Search/>
          <MostRecentRecipes posts={props.posts}/>
          {/*<VideoDemo/>*/}
          {/*<WhyKalico/>*/}
          {/*<HowItWorks/>*/}
        </main>
      </>
  );
}

export default RecipeLanding;
