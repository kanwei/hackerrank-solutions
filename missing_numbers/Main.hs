{-# LANGUAGE OverloadedStrings #-}

module Main where

import Data.List
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString as BS
import qualified Data.Text.Lazy as L
import qualified Data.Text.Lazy.IO as LIO

l1 = "10000 203 204 205 206 207 208 203 204 205 206"
l2 = "203 204 204 205 206 207 205 208 203 206 205 206 204"


-- createFreqmap :: [a] -> pval -> [(a, Integer)] -> [(a, Integer)]
createFreqmap [] pval acc = acc
createFreqmap (x:xs) pval acc = createFreqmap xs pval (M.insertWith (+) x pval acc)

freq1 = createFreqmap (L.words l1) 1 (M.fromList [])
freq2 = createFreqmap (L.words l2) (-1) freq1

noZeroes = M.filterWithKey (\x y -> y /= 0)

ordinalSort :: [L.Text] -> [Int]
ordinalSort = sort . (map ((read::String->Int) .  L.unpack))

s = unwords $ map show $ ordinalSort $ M.keys $ noZeroes freq2

main = do
    _ <- getLine
    l1 <- LIO.getLine
    _ <- getLine
    l2 <- LIO.getLine
    let freq1 = createFreqmap (L.words l1) 1 (M.fromList [])
    let freq2 = createFreqmap (L.words l2) (-1) freq1
    putStrLn $ unwords $ map show $ ordinalSort $ M.keys $ noZeroes freq2