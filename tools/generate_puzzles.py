import csv
import io
import json
import os
import urllib.request

import chess
import zstandard

URL = "https://database.lichess.org/lichess_db_puzzle.csv.zst"
OUTPUT = "app/src/main/assets/puzzles.json"
LIMIT = 1000


def main():
    os.makedirs(os.path.dirname(OUTPUT), exist_ok=True)

    print("Downloading Lichess puzzle database...")

    request = urllib.request.urlopen(URL)

    decompressor = zstandard.ZstdDecompressor()

    puzzles = []
    seen = set()

    with decompressor.stream_reader(request) as reader:
        text = io.TextIOWrapper(reader, encoding="utf-8")
        rows = csv.DictReader(text)

        for row in rows:
            try:
                rating = int(row["Rating"])
                popularity = int(row["Popularity"])

                if not 1000 <= rating <= 2200:
                    continue

                if popularity < 50:
                    continue

                moves = row["Moves"].split()

                board = chess.Board(row["FEN"])

                if not moves:
                    continue

                # Apply the opponent's last move.
                board.push_uci(moves[0])

                puzzle_fen = board.fen()
                solution = moves[1:]

                if not solution:
                    continue

                # Validate every solution move.
                for move in solution:
                    board.push_uci(move)

                puzzle_id = row["PuzzleId"]

                if puzzle_id in seen:
                    continue

                seen.add(puzzle_id)

                puzzles.append({
                    "id": puzzle_id,
                    "fen": puzzle_fen,
                    "moves": solution,
                    "rating": rating,
                    "themes": row["Themes"].split()
                })

                if len(puzzles) >= LIMIT:
                    break

            except (ValueError, KeyError, chess.InvalidMoveError,
                    chess.IllegalMoveError, chess.AmbiguousMoveError):
                continue

    with open(OUTPUT, "w", encoding="utf-8") as file:
        json.dump(puzzles, file, ensure_ascii=False)

    print("Generated", len(puzzles), "puzzles.")


if __name__ == "__main__":
    main()
