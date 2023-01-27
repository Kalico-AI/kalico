import moment from "moment";

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