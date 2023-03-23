import React, {useState} from 'react';
import {Box} from "@mui/material";
import SearchBar from "material-ui-search-bar";


function Search() {
  const [value, setValue] = useState("")

  const handleSubmit = () => {
    console.log("submitting search request...: ", value)
  }
  return (
            <Box className="search-bar-container">
            <SearchBar
                value={value}
                placeholder="https://www.youtube.com/watch?v=mCNH9rn2OS0"
                onChange={(newValue) => setValue(newValue)}
                onRequestSearch={handleSubmit}
            />
            </Box>
  );
}

export default Search;
