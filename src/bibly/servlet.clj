(ns bibly.servlet
  (:gen-class :extends com.google.wave.api.AbstractRobotServlet)
  (:import
    [com.google.appengine.api.users UserServiceFactory]
    [com.google.wave.api Blip Event EventType RobotMessageBundle TextView Wavelet])
  (:require [clojure.contrib.str-utils :as str-utils]
            [clojure.contrib.str-utils2 :as str-utils2]))
 
(defn blip-submitted-events
  [events]
  (filter (fn [e] (= (EventType/BLIP_SUBMITTED) (.getType e))) events))

(defn roll-die
  "Roll a die with <sides> sides."
  [sides]
  ;(println (str "rolling dice, sides: " sides))
  (+ 1 (rand-int sides)))

(defn calculate-rolls
  "Given a re-pattern match group, calculate the dice roll"
  [txt]
  (let [string (first txt)
        dice   (Integer. (second txt))
        sides  (Integer. (nth txt 2))]
    ;(println (str "dice: " dice))
    ;(println (str "sides: " sides))
    (str string " (" (reduce + (take dice (repeatedly #(roll-die sides)))) ")")))

(defn roll
  "Given a text DnD dice roll, replace it with the calculated result."
  [text]
  (str-utils2/replace text (re-pattern #"(\d+)d(\d+)") calculate-rolls))

(defn replace-rolls
  "Replace all the rolls in a given blip."
  [blip]
  (let [view (.getDocument blip)
        text (.getText view)]
    (.replace view (roll text))))

(defn -processEvents
  [this bundle]
  (let [wavelet (.getWavelet bundle)]
    (doseq [event (blip-submitted-events (.getEvents bundle))]
      (replace-rolls (.getBlip event)))))

