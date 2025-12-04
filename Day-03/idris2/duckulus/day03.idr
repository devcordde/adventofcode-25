import System.File
import Data.String

read : String -> IO (Either FileError String)
read path = do
  text <- readFile path
  pure $ map trim text

digitToInt : Char -> Nat 
digitToInt c = cast (ord c - ord '0')

parse : String -> List (List Nat)
parse str = map (map  digitToInt . unpack) (lines str)

maxWithIndex: Ord a => List a -> Maybe (a, Nat)
maxWithIndex [] = Nothing
maxWithIndex [x] = Just (x, 0)
maxWithIndex (x :: xs) = case maxWithIndex xs of
                     Just (m, i) => Just $ if m > x then (m, i+1) else (x, 0)
                     _ => Nothing
  
maxInRange : Ord a => List a -> Nat -> Nat -> Maybe (a, Nat)
maxInRange xs l off = map (\(x,i) => (x,i+l)) $ maxWithIndex (take off (drop l xs))

pow : Nat -> Nat -> Nat
pow n 0 = 1
pow n m = n * (n `pow` (m `minus` 1))

maxJoltage : (d : Nat) -> List Nat -> Maybe Nat
maxJoltage 0 _ = Just 0
maxJoltage d bank =
  let Just (max, maxi) = maxInRange bank 0 ((length bank `minus` d) + 1) | Nothing => Nothing 
      next_d = d `minus` 1
      current_digit_value = max * (10 `pow` next_d) 
      Just remaining_digits_value = maxJoltage next_d (drop (maxi + 1) bank) | Nothing => Nothing in
      Just $ current_digit_value + remaining_digits_value

main : IO ()
main = do
  Right text <- read "input.txt"
  | Left err => printLn $ "Error: " ++ show err
  printLn $ traverse (maxJoltage 2) (parse text) |> map sum
  printLn $ traverse (maxJoltage 12) (parse text) |> map sum

