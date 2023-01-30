import moment from "moment";
import {Box, CircularProgress} from "@mui/material";
import React from "react";

export const getFormattedDate = (date: number) => {
  return moment.unix(date).format("llll")
}

export const sessionIdSet = (req): boolean => {
  const cookies = {};
  req.headers.cookie.split(';').forEach(function(cookie) {
    const parts = cookie.match(/(.*?)=(.*)$/)
    cookies[parts[1].trim()] = (parts[2] || '').trim();
  });
   return !!cookies['sessionId'];

}

export const CenterAlignedProgress = () => {
  return (
      <Box className='center-aligned-progress'>
        <CircularProgress />
      </Box>
  )
}