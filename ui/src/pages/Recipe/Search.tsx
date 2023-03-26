import React, {useState} from 'react';
import {Box, CircularProgress} from "@mui/material";
import SearchBar from "material-ui-search-bar";
import {RecipeApi} from "@/api";
import {useRouter} from "next/router";


function Search() {
  const [value, setValue] = useState("")
  const [showProgress, setShowProgress] = useState(false)
  const [error, setError] = useState('')
  const [status, setStatus] = useState('')
  const router = useRouter()

  const handleSubmit = () => {
    setShowProgress(true)
    setError('')
    new RecipeApi()
    .createRecipe({value: value})
    .then(result => {
      setShowProgress(false)
      if (result.data) {
        if (result.data.slug) {
          router.push("/recipe/" + result.data.slug)
          .then(_ => {
            setError('')
          })
          .catch(e => console.log(e))
        } else if (result.data.status) {
          setStatus(result.data.status)
        } else if (result.data.error) {
          setError(result.data.error)
        }
      } else {
        setError("Sorry, something went wrong. Please check your network connection.")
      }
    }).catch(e => console.log(e))
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
              {error && <p className="error">{error}</p>}
              {status && <p className="success">{status}</p>}
            </Box>
  );
}

export default Search;
