import React, {useState} from 'react';
import {Box, CircularProgress} from "@mui/material";
import SearchBar from "material-ui-search-bar";


function Search() {
  const [value, setValue] = useState("")
  const [showProgress, setShowProgress] = useState(false)
  const [error, setError] = useState('d')

  const handleSubmit = () => {
    setShowProgress(true)
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
              {showProgress &&
                  <Box className="search-progress">
                    <Box className="progress-box">
                      <CircularProgress/>
                    </Box>
                    <Box className="text">
                      <span>This will take just a minute</span>
                    </Box>
                  </Box>
              }
              {error && <p className="error">Sorry, that link that does not contain a food recipe</p>}
            </Box>
  );
}

export default Search;
