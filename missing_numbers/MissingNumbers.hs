module Main where

import qualified Data.Set as S
import qualified Data.Map as M

l1 = "203 204 205 206 207 208 203 204 205 206"
l2 = "203 204 204 205 206 207 205 208 203 206 205 206 204"

mapStringsToInt = (map (read::String->Integer) . words)

l1w = mapStringsToInt l1
l2w = mapStringsToInt l2

-- createFreqmap :: [a] -> pval -> [(a, Integer)] -> [(a, Integer)]
createFreqmap [] pval acc = acc
createFreqmap (x:xs) pval acc = createFreqmap xs pval (M.insertWith (+) x pval acc)

freq1 = createFreqmap l1w 1 (M.fromList [])
freq2 = createFreqmap l2w (-1) freq1

noZeroes = M.filterWithKey (\x y -> y /= 0)

s = unwords $ map show $ M.keys $ noZeroes freq2

-- main = 

main = do
    _ <- getLine
    l1w <- getLine
    _ <- getLine
    l2w <- getLine
    let freq1 = createFreqmap (mapStringsToInt l1w) 1 (M.fromList [])
    let freq2 = createFreqmap (mapStringsToInt l2w) (-1) freq1
    putStrLn $ unwords $ map show $ M.keys $ noZeroes freq2