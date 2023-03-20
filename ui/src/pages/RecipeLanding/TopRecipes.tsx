import React from 'react';
import {Container} from "@mui/system";
import ListView from "@/pages/RecipeLanding/ListView";
import {Box} from "@mui/material";


function TopRecipes() {
  return (
  <Container className="top-recipes">
    <Box className="top-recipes-title">
      <h4>Top Recipes</h4>
    </Box>
    <ListView/>
    <Box className="top-recipes-view-all">
        <button
            className="view-all-button"
        >View All</button>
    </Box>
  </Container>

  );
}

export default TopRecipes;
