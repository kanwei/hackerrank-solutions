{-# LANGUAGE OverloadedStrings #-}

module Main where
import Data.List
import qualified Data.Map as M
-- import qualified Data.ByteString as BS
import qualified Data.ByteString.Char8 as L
-- import qualified Data.ByteString.IO as LIO

createFreqmap [] pval acc = acc
createFreqmap (x:xs) pval acc = createFreqmap xs pval (M.insertWith (+) x pval acc)

noZeroes = M.filter (/= 0)

-- ordinalSort :: [L.Text] -> [Int]
ordinalSort = sort . (map ((read::String->Int) .  L.unpack))

main = do
    _ <- getLine
    l1 <- L.getLine
    _ <- getLine
    l2 <- L.getLine
    let freq1 = createFreqmap (L.words l1) 1 (M.fromList [])
    let freq2 = createFreqmap (L.words l2) (-1) freq1
    putStrLn $ unwords $ map show $ ordinalSort $ M.keys $ noZeroes freq2
    