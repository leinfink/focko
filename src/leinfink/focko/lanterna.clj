(ns leinfink.focko.lanterna
  (:gen-class)
  (:import
   (com.googlecode.lanterna TerminalPosition
                            TerminalSize
                            TextColor$ANSI)
   (com.googlecode.lanterna.screen TerminalScreen)
   (com.googlecode.lanterna.terminal DefaultTerminalFactory
                                     SimpleTerminalResizeListener)))

(def colors {:black TextColor$ANSI/BLACK
             :white TextColor$ANSI/WHITE
             :cyan TextColor$ANSI/CYAN})

(defn create-terminal [emulate]
  (let [factory (DefaultTerminalFactory.)]
    (if emulate
      (.createTerminalEmulator factory)
      (.createTerminal factory))))

(defn create-screen
  ([] (create-screen nil))
  ([emulate] (TerminalScreen. (create-terminal emulate))))

(defmacro with-screen [[scr screen-expr] & body]
  `(let [~scr ~screen-expr]
     (try
       (.startScreen ~scr)
       ~@body
       (finally (.stopScreen ~scr)))))

(defn create-graphics [screen] (.newTextGraphics screen))

(defn refresh [screen] (.refresh screen))

(defn clear [screen] (.clear screen))

(defn get-size [tgx]
  (let [size (.getSize tgx)]
    [(.getColumns size) (.getRows size)]))

(defn position [x y] (TerminalPosition. x y))

(defn size [width height] (TerminalSize. width height))

(defn normalize [[x y] [width height]]
  (let [norm (fn [n maximum] (if (neg? n) (+ n maximum) n))]
    [(norm x width) (norm y height)]))

(defn put-string [tgx [x y] str & {:keys [fg-color bg-color]}]
  (let [[x y] (normalize [x y] (get-size tgx))]
    (when fg-color (.setForegroundColor tgx (fg-color colors)))
    (when bg-color (.setBackgroundColor tgx (bg-color colors)))
    (.putString tgx x y str)))

(defn put-rect
  ([tgx pos size] (put-rect tgx pos size nil))
  ([tgx [x y] [width height] color]
   (when color (.setBackgroundColor tgx (color colors)))
   (.fillRectangle tgx (position x y) (size width height) \space)))

(defn read-input [screen]
  (let [keystroke (.readInput screen)]
    (str (.getCharacter keystroke)))
  )

(comment

  (with-screen [scr (create-screen true)]
    (doto (create-graphics scr)
      (put-string [25 20] "@" :fg-color :black :bg-color :cyan)
      (put-rect [20 20] [2 2] :cyan))
    (refresh scr)
    (read-input scr))

  ,)
