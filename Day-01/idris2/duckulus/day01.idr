import System.File
import Data.String
import Data.Maybe

isEmpty : String -> Bool
isEmpty = (== "") . trim

readLines : String -> IO (Either FileError (List String))
readLines path = do
  Right text <- readFile path | Left err => pure $ Left err
  pure (Right $ filter (not . isEmpty) $ lines text)


data Direction = L | R

record Rotation where
  constructor MkRotation
  direction : Direction
  amount : Int

parseDirection : Char -> Maybe Direction
parseDirection c = case c of
                        'R' => Just R
                        'L' => Just L
                        _ => Nothing

toInt : Direction -> Int
toInt R = 1
toInt L = -1


parseRotation : String -> Maybe Rotation
parseRotation str = do
                    StrCons x xs <- Just $ strM str | StrNil => Nothing
                    dir <- parseDirection x
                    amount <- parseInteger xs
                    Just $ MkRotation dir amount

partOne : List String -> Int
partOne ls = go (mapMaybe parseRotation ls) 50 0
where
  go: List Rotation -> Int -> Int -> Int
  go [] _ count = count
  go ((MkRotation dir amount) :: xs) dial count = 
    let newDial = mod (dial + (toInt dir * amount)) 100
        newCount = if newDial == 0 then count + 1 else count
    in go xs newDial newCount 

partTwo : List String -> Int
partTwo ls = go (mapMaybe parseRotation ls) 50 0
where
  go: List Rotation -> Int -> Int -> Int
  go [] _ count = count
  go ((MkRotation dir amount) :: xs) dial count = case dir of 
    L => let temp := dial - amount
             newDial = temp `mod` 100
             newCount = count + (abs $ div (dial - amount) 100) + (if newDial == 0 then 1 else 0) + (if dial == 0 && temp < 0 then -1 else 0)
             in go xs newDial newCount
    R => let temp = dial + amount
             newDial = temp `mod` 100
             newCount = count + (div temp 100)
             in go xs newDial newCount
    
main : IO ()
main = do
  Right ls <- readLines "./input.txt" | Left err => printLn $ "Error: " ++ show err
  printLn $ partOne ls 
  printLn $ partTwo ls 


