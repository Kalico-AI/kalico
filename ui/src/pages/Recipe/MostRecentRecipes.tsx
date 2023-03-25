import React, {FC} from 'react';
import {Container} from "@mui/system";
import {Box} from "@mui/material";
import GridView from "@/pages/Recipe/GridView";
import {urls} from "@/utils/paths";
import Link from "next/link";

export interface MostRecentRecipesProps {
  posts: []
}
const MostRecentRecipes: FC<MostRecentRecipesProps> =  (props) => {
  // const router = useRouter()
  //
  // const onViewAll = () => {
  //   router.push(urls.getAllRecipes)
  //   .catch(e => console.log(e))
  // }
  return (
  <Container className="top-recipes">
    <Box className="top-recipes-title">
      <h4>Most Recent Recipes</h4>
    </Box>
    <GridView posts={props.posts}/>
    <Box className="top-recipes-view-all">
      <Link href={urls.getAllRecipes}>
        <button
            className="view-all-button"
        >View All</button>
      </Link>
    </Box>
  </Container>

  );
}

export default MostRecentRecipes;
